import cv2, imutils, numpy as np, io, base64
from imutils import contours
from imutils.perspective import four_point_transform
from PIL import Image

def show(name,imagetoshow,standard=500):
	h,w = imagetoshow.shape[0:2]
	rate = -1
	if h>=w:
		w=standard/h*w
		h=standard
	else:
		h=standard/w*h
		w=standard
	imagetoshow = cv2.resize(imagetoshow,(int(w),int(h)))
	cv2.imshow(str(name),imagetoshow)

def sortArray4point(listCntIn):
	listCntInCop = listCntIn.copy()
	for i in range(len(listCntInCop)):
		listCntInCop[i] = sort4point(listCntInCop[i])
	return listCntInCop

def sort4point(CntIn):
	cnttmp = CntIn.copy()
	cnttmp = np.array(  contours.sort_contours(cnttmp, method="left-to-right")[0]  )
	cnttmp[0:2] = np.array(  contours.sort_contours(cnttmp[0:2], method="top-to-bottom")[0]  )
	cnttmp[2:4] = np.array(  contours.sort_contours(cnttmp[2:4], method="bottom-to-top")[0]  )
	return cnttmp

def getCnt(img,minVal=-1,maxVal=-1):
	# print('========== begin =========')
	imggray =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	# blurred = cv2.GaussianBlur(imggray, (3, 3), 0)
	# edged = cv2.Canny(blurred, 50, 200)

	edged = cv2.adaptiveThreshold(imggray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 11, 2)
	edged = cv2.bitwise_not(edged)
# 	show('edged',edged,900)
	# cv2.waitKey(0)

	cnts = cv2.findContours(edged.copy(), cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
	cnts = imutils.grab_contours(cnts)

	neoCnt = []
	cnts = sorted(cnts, key=cv2.contourArea, reverse=True)

	img1 = img.copy()

	h,w = img.shape[0:2]

	for c in cnts:
		peri = cv2.arcLength(c, True)
		approx = cv2.approxPolyDP(c, 6, True)
		area = cv2.contourArea(approx)

		# print(len(approx))
		# if area >= h*w*80/100:
		# 	x,y,w,h = cv2.boundingRect(approx)
		# 	# cv2.rectangle(img1,(x,y),(x+w,y+h),(0,255,0),3)
		# 	cv2.rectangle(img1,(0,0),(150,150),(0,255,0),3)
		# 	cv2.drawContours(img1, c, -1, (0,255,0), 3)
		# 	show('img1',img1,900)
		# 	cv2.waitKey(0)
		# 	img = four_point_transform(img, approx.reshape(4, 2))
		# 	return []
		
		if len(approx)==4 and ((minVal<=area and area<=maxVal and minVal!=-1 and maxVal!=-1)
			or (minVal<=area and minVal!=-1 and maxVal==-1)
			or (area<=maxVal and minVal==-1 and maxVal!=-1)
			or (minVal==-1 and maxVal==-1)):
			neoCnt.append(approx)
			# print(area)
			# (x, y, w, h) = cv2.boundingRect(c)
			# ar = w / float(h)
			# if w >= 15 and h >= 15 and ar >= 0.85 and ar <= 1.15:
				# cv2.drawContours(img1, [c], -1, (0,255,0), 3)
				# cv2.rectangle(img1, (x,y), 
				# 					(x+w,y+h), (0, 0, 255), 3)
				# print('w: {} h:{}'.format(w,h))
	# for i in range(len(neoCnt)):
	# 	# if i!=28:
	# 	# 	continue
	# 	cv2.putText(img1, "{}".format(i), (neoCnt[i][0][0][0], neoCnt[i][0][0][1]),
	# 							cv2.FONT_HERSHEY_SIMPLEX, 1.75, (0, 0, 255), 2)
	# cv2.drawContours(img1,neoCnt, -1, (0,255,0), 3)
# 	print(len(cnts))
# 	print(minVal)
# 	print(maxVal)
# 	print(len(neoCnt))

	# cv2.drawContours(img1, cnts, -1, (0,255,0), 3)
	cv2.drawContours(img1, neoCnt, -1, (0,255,0), 3)
# 	show('img1',img1,900)
	# show('img',img,900)

	neoCnt = sortArray4point(neoCnt)
	# print(len(neoCnt))
	# for i in range(0,len(neoCnt)):
	# 	pnt = np.round(cv2.mean(neoCnt[i])[0:2]).astype("int")
	# 	print('[{},{}],'.format(pnt[0],pnt[1]))
	# print('========== end =========')
	# cv2.waitKey(0)
	return neoCnt

def transform_img_input(img):
	minnn, maxxx = 1600, 2200
	listCnt = np.array(getCnt(img,minnn,maxxx))
	while len(listCnt) != 8:
		# print(len(listCnt))
		# if len(listCnt) < 8:
		minnn = minnn - 100
		maxxx = maxxx - 100
		if minnn <= 0:
			return
		# else:
		# minnn = minnn + 100
		# maxxx = maxxx + 100
		listCnt = np.array(getCnt(img,minnn,maxxx))

	# print(len(listCnt))

	shapecvt = np.array([[0,0]]*4)
	
	cntleft = np.array(  contours.sort_contours(listCnt, method="left-to-right")[0][0:3]  )
	cntleft = np.array(  contours.sort_contours(cntleft, method="top-to-bottom")[0]  )
	cntleft = sortArray4point(cntleft)

	cntright = np.array(  contours.sort_contours(listCnt, method="right-to-left")[0][0:3]  )
	cntright = np.array(  contours.sort_contours(cntright, method="top-to-bottom")[0]  )
	cntright = sortArray4point(cntright)

	# tmpcnt = np.array((cntleft[0][0][0],cntleft[0][1][0],cntleft[0][2][0],cntleft[0][3][0]))
	shapecvt[0] = cntleft[0][0] + (-10,-10)
	shapecvt[1] = cntleft[2][1] + (-10, 10)
	shapecvt[2] = cntright[2][2] + (10, 10)
	shapecvt[3] = cntright[0][3] + (10,-10)

	imgcopy = four_point_transform(img, shapecvt.reshape(4, 2))
	# for i in range(len(shapecvt)):
	# 	cv2.putText(img, "{}".format(i), (shapecvt[i][0], shapecvt[i][1]),
	# 							cv2.FONT_HERSHEY_SIMPLEX, 1.75, (0, 0, 255), 2)
	# cv2.drawContours(imgcopy,listCnt, -1, (0,255,0), 3)
	# print(cntleft[0][0])

	# cv2.rectangle(img, (cntleft[0][0][0][0],cntleft[0][0][0][1]), (cntleft[0][2][0][0],cntleft[0][2][0][1]), (0, 0, 255), 3)
	# cv2.rectangle(img, (cntleft[2][0][0][0],cntleft[2][0][0][1]), (cntleft[2][2][0][0],cntleft[2][2][0][1]), (0, 0, 255), 3)
	# cv2.rectangle(img, (cntright[2][0][0][0],cntright[2][0][0][1]), (cntright[2][2][0][0],cntright[2][2][0][1]), (0, 0, 255), 3)

	# show('imgcopy',imgcopy,900)
	# show('tesstimg',img,900)
	# cv2.waitKey(0)
	return imgcopy

def cropimg(img,point_start,point_end):
	x1, y1 = point_start[0], point_start[1]
	x2, y2 = point_end[0], point_end[1]
	x, y = min(x1, x2), min(y1, y2)
	w, h = abs(x2-x1), abs(y2-y1)
	cropped = img[y:y+h, x:x+w]
	return cropped

def crop_1(img,listCnt):
	shapecvt = np.array([[0,0],[0,0]])
	cntsbd = np.array(contours.sort_contours(listCnt, method="left-to-right")[0][3:8])
	cntsbd1 = np.array(contours.sort_contours(cntsbd[0:2], method="top-to-bottom")[0])
	# print(cntsbd1[0][2][0][0])
	shapecvt[0] = np.array((cntsbd1[0][2][0][0],cntsbd1[0][2][0][1]+50))

	cntsbd2 = np.array(contours.sort_contours(cntsbd[2:5], method="top-to-bottom")[0])
	shapecvt[1] = cntsbd2[1][0]

	imgcopy = cropimg(img,shapecvt[0],shapecvt[1])
	return imgcopy

def crop_2(img,listCnt):
	shapecvt = np.array([[0,0],[0,0]])
	cntans = np.array(contours.sort_contours(listCnt, method="top-to-bottom")[0][3:8])
	cntans = np.array(contours.sort_contours(cntans, method="left-to-right")[0])
	cntans[0:2] = np.array(contours.sort_contours(cntans[0:2], method="top-to-bottom")[0])
	cntans[3:5] = np.array(contours.sort_contours(cntans[3:5], method="top-to-bottom")[0])

	cntans = sortArray4point(cntans)
	shapecvt[0] = np.array((cntans[0][2][0][0],cntans[0][2][0][1]+70))
	shapecvt[1] = cntans[4][0]

	imgcopy = cropimg(img,shapecvt[0],shapecvt[1])
	# show('imgtest',imgcopy,900)
	# cv2.waitKey(0)
	return imgcopy

def crop_ans_to_10_img(img,listCnt):
	cntans = np.array(contours.sort_contours(listCnt, method="left-to-right")[0])
	cntans[1:7] = np.array(contours.sort_contours(cntans[1:7], method="top-to-bottom")[0])
	cntans[7:13] = np.array(contours.sort_contours(cntans[7:13], method="top-to-bottom")[0])
	listleft = [np.round(cv2.mean(cntans[0])[0:2]).astype("int")]
	listright = []
	for i in range(2,7):
		listright.append(np.round(cv2.mean(cntans[i])[0:2]).astype("int"))
		if i!=6:
			listleft.append([listleft[i-2][0],listright[i-2][1]])
	point = np.round(cv2.mean(cntans[13])[0:2]).astype("int")
	for i in range(7,12):
		listleft.append(np.round(cv2.mean(cntans[i])[0:2]).astype("int"))
		p1 = [point[0],np.round(cv2.mean(cntans[i+1])[0:2]).astype("int")[1]]
		listright.append(p1)

	listleft = np.array(listleft)
	listright = np.array(listright)
	listimg = []
	for i in range(0,10):
		imgcopy = cropimg(img, listleft[i], (listright[i][0],listright[i][1]))
		listimg.append(imgcopy)
	return listimg

def findAns(img,listCircle,num,isVertical,strAnswer=None):
	if isVertical==True:
		listCircle = np.array(sorted(listCircle, key=lambda k: k[0]))
	else:
		listCircle = np.array(sorted(listCircle, key=lambda k: k[1]))
	thresh = cv2.threshold(cv2.cvtColor(img.copy(), cv2.COLOR_BGR2GRAY), 0, 255, cv2.THRESH_BINARY_INV | cv2.THRESH_OTSU)[1]
	anschoose = []
	stringans = str(strAnswer)
	dem=0
	for (col, index) in enumerate(np.arange(0, len(listCircle), num)):
		cnts = listCircle[index:index+num]
		if isVertical==True:
			cnts = np.array(sorted(cnts, key=lambda k: k[1]))
		else:
			cnts = np.array(sorted(cnts, key=lambda k: k[0]))

		idTrueAns = -1
		if strAnswer != None:
			if int(index/4)>=len(stringans):
				break
			idTrueAns = ['A','B','C','D'].index(stringans[int(index/4)])

		bubbled = []
		for (j,(x,y)) in enumerate(cnts):
			mask = np.zeros(thresh.shape, dtype="uint8").copy()
			cv2.circle(mask, (x, y), 10, 255, -1)
			mask = cv2.bitwise_and(thresh, thresh, mask=mask)
			total = cv2.countNonZero(mask)
			if total > 220:
				bubbled.append(j)
		if strAnswer != None:
			if len(bubbled)==0:
				color = (0,255,255)
				x,y = cnts[idTrueAns]
				cv2.circle(img, (x, y), 14, color, 3)
				anschoose.append('#')
			elif len(bubbled)==1:
				if idTrueAns==bubbled[0]:
					dem = dem+1
					color = (0,255,0) 
					x,y = cnts[bubbled[0]]
					cv2.circle(img, (x, y), 14, color, 3)
					anschoose.append(bubbled[0])
				else:
					color = (0,255,255)  
					x,y = cnts[idTrueAns]
					cv2.circle(img, (x, y), 14, color, 3)
					color = (0,0,255)  
					x,y = cnts[bubbled[0]]
					cv2.circle(img, (x, y), 14, color, 3)
					anschoose.append(bubbled[0])
			else:
				color = (0,0,255)  
				for i in range(0,len(bubbled)):
					x,y = cnts[bubbled[i]]
					cv2.circle(img, (x, y), 14, color, 3)
				color = (0,255,255)
				x,y = cnts[idTrueAns]
				cv2.circle(img, (x, y), 14, color, 3)
		else:
			color = (0,255,0)
			if len(bubbled)!=1:
				color = (0,0,255)
				anschoose.append('#')
			else:
				anschoose.append(bubbled[0])
			for j in bubbled:
				x,y = cnts[j]
				cv2.circle(img, (x, y), 14, color, 3)
	# show('imggggg',img,900)
	# cv2.waitKey(0)
	return (''.join(map(str,anschoose)),dem)

def get_Sbd_and_maDe(img,returnMade):
	# show('sbd-made',img.copy(),900)
	minnn, maxxx = 400,800
	neoCnt = getCnt(img.copy(),minnn,maxxx)
	while len(neoCnt) != 3:
		minnn = minnn - 100
		maxxx = maxxx - 100
		if minnn <= 0:
			return
		neoCnt = getCnt(img.copy(),minnn,maxxx)
	# print(minnn)
	# print(maxxx)
	neoCnt = np.array(contours.sort_contours(neoCnt, method="top-to-bottom")[0])
	point0 = np.round(cv2.mean(neoCnt[0])[0:2]).astype("int")
	point2 = np.round(cv2.mean(neoCnt[2])[0:2]).astype("int")
	w = img.shape[1]
	img2 = cropimg(img,(point2[0]+20,point0[1]),(w,point2[1]))
	made = get_Made(img2)
	if returnMade==True:
		# print(made)
		return made

	img1 = cropimg(img,(0,point0[1]),(point2[0]-20,point2[1]))
	sbd = get_Sbd(img1)

	# show('sbd',img1.copy(),900)
	# show('made',img2.copy(),900)

	# cv2.waitKey(0)
	return sbd,made

def get_Sbd(img1):
	circles1 = detect_circlesv2(img1,10,6)
	sbd = findAns(img1,circles1,10,True)
	return sbd

def get_Made(img2):
	circles2 = detect_circlesv2(img2,10,3)
	made = findAns(img2,circles2,10,True)
	return made

def get_ans(img,stringans):
	minnn, maxxx = 700,1300
	neoCnt = getCnt(img.copy(),minnn,maxxx)
	while len(neoCnt) != 14:
		minnn = minnn - 100
		maxxx = maxxx - 100
		if minnn <= 0: 
			return
		neoCnt = getCnt(img.copy(),minnn,maxxx)
	listIMGcrop = crop_ans_to_10_img(img,neoCnt)

	# cnt = getCnt(listIMGcrop[0])
	# print(len(cnt))

	listans = []
	for i in range(0,int(len(stringans)/5+1)):
		circles = detect_circlesv2(listIMGcrop[i],5,4)
		# print(str(i)+'  '+str(len(circles)))
		stringansss = 'AA'
		if i*5+5 < len(stringans):
			stringansss = stringans[i*5:i*5+5]
		else:
			stringansss = stringans[i*5:len(stringans)]
		# print(stringansss)
		ans = findAns(listIMGcrop[i],circles,4,False,stringansss)
		listans.append(ans)
		# print('ans   '+str(ans))
		# show('listIMGcrop[{}]'.format(i),listIMGcrop[i],900)
	return listans

def detect_circlesv2(img,row,cel):
	if row == 5:
		return np.array(([178,221],[280,173],[484,124],[280,26],[484,222],
						[484,76],[280,75],[484,173],[280,124],[280,222],
						[484,26],[384,125],[178,26],[178,75],[383,26],
						[178,173],[178,124],[383,74],[383,222],[382,173]))
	rows, cols = img.shape[:2]
	cell_width = cols / cel
	cell_height = rows / row

	# print('wid: '+str(cell_width))
	# print('hei: '+str(cell_height))
	circles = []
	for i in range(cel):
		for j in range(row):
			# print(str(i)+" "+str(j))
			circles.append(np.array((round(cell_width*i+cell_width/2),round(cell_height*j+cell_height/2))))

	# for (x,y) in tmp:
	# 	cv2.circle(img, (x, y), 11, (0,0,255), 2)
	# cv2.imshow("Result", img)
	# cv2.waitKey(0)
	return circles

def detect_circles(img):
	imggray =  cv2.cvtColor(img.copy(), cv2.COLOR_BGR2GRAY)
	blurred = cv2.GaussianBlur(imggray, (1, 1), 0)
	circles = cv2.HoughCircles(blurred, cv2.HOUGH_GRADIENT, 1, 30, param1=40, param2=10, minRadius=9, maxRadius=14)[0]
	if circles is not None:
		circles = np.round(circles[::]).astype("int")
	for (x,y,r) in circles:
		cv2.circle(img, (x, y), 11, (0,0,0), 1)

# 	show("Result", img, 900)
	cv2.waitKey(0)
	return circles

def img2string(img):
	retval, buffer = cv2.imencode('.jpg', img)
	jpg_as_text = base64.b64encode(buffer)
	jpg_as_text = jpg_as_text.decode('utf-8')
	return str(jpg_as_text)

def string2img(stringIMG):
    imgdata = base64.b64decode(str(stringIMG))
    img = np.array(cv2.imdecode(np.frombuffer(imgdata, np.uint8), -1))
    return img

def run1(stringimage):
	try:
		# imgpath = 'images/Test22-choose.png'
		# img = cv2.imread(imgpath).copy()
		img = string2img(stringimage)
		img = cv2.resize(img,(1654,2339),interpolation = cv2.INTER_CUBIC)
		# brightened_img = cv2.convertScaleAbs(img, alpha=1.2, beta=0)
		# img = brightened_img
		# gray =  cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
		# thresh = cv2.adaptiveThreshold(gray, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 5, 2)
		# show('thresh',thresh,1000)
		# cv2.waitKey(0)
		img = transform_img_input(img)
		img = cv2.resize(img,(1466,2136),interpolation = cv2.INTER_CUBIC)
		# show('img',img,1000)
		# cv2.waitKey(0)

		minnn1, maxxx1 = 1500,2100
		neoBigCnt = getCnt(img,minnn1,maxxx1)
		while len(neoBigCnt) != 8:
			minnn1 = minnn1 - 100
			maxxx1 = maxxx1 - 100
			if minnn1 <= 0:
				neoBigCnt = None
				break
			neoBigCnt = getCnt(img,minnn1,maxxx1)
		neoBigCnt = np.array(neoBigCnt)

		img_sbd_and_made = crop_1(img,neoBigCnt)
		maDe = get_Sbd_and_maDe(img_sbd_and_made,True)

		# print('made: '+maDe)
		if '#' in maDe[0]:
			return '###'
		else:
			return maDe[0]
	except Exception as ex:
# 		print('Lỗi run1: ',str(ex))
		return '###'

def run2(stringimage,ansTrue,HeDiem):
	try:
		img = string2img(stringimage)
		img = cv2.resize(img,(1654,2339),interpolation = cv2.INTER_CUBIC)
		img = transform_img_input(img)
		img = cv2.resize(img,(1466,2136),interpolation = cv2.INTER_CUBIC)

		minnn1, maxxx1 = 1500,2100
		neoBigCnt = getCnt(img,minnn1,maxxx1)
		while len(neoBigCnt) != 8:
			minnn1 = minnn1 - 100
			maxxx1 = maxxx1 - 100
			if minnn1 <= 0:
				neoBigCnt == None
				break
			neoBigCnt = getCnt(img,minnn1,maxxx1)
		neoBigCnt = np.array(neoBigCnt)

		img_sbd_and_made = crop_1(img,neoBigCnt)
		sbd, maDe = get_Sbd_and_maDe(img_sbd_and_made,False)

		img_ans = crop_2(img,neoBigCnt)
		anschoose = get_ans(img_ans,ansTrue)

		diemMoiCau = HeDiem/len(ansTrue)
		sumdiem = 0
		# print('diem moi cau    '+str(diemMoiCau))

		for tup in anschoose:
			sumdiem = sumdiem + diemMoiCau*tup[1]

		cv2.putText(img, "{}/{}".format(np.round(sumdiem,2),HeDiem), (740,570),
								cv2.FONT_HERSHEY_SIMPLEX, 1.75, (0,0,255), 4)

		sumdiem = np.round(sumdiem,3)
		# print('sumdiem    '+str(sumdiem))
		# show('img',img,1000)
		# show('img_sbd_and_made',img_sbd_and_made,700)
		# show('img_ans',img_ans,900)
		# cv2.waitKey(0)
		stringimgreturn = img2string(img)
		if '#' not in sbd[0]:
			return '{}#####{}#####{}'.format(sbd[0],sumdiem,stringimgreturn)
		else:
			return '000000#####{}#####{}'.format(sumdiem,stringimgreturn)
	except Exception as ex:
# 		print('Lỗi run2: ', str(ex))
		return '999999#####0.0#####{}'.format(stringimage)

########## end ##########

# import pathlib, os
# data_dir = pathlib.Path('images')
# image_list = list(data_dir.glob('*.png'))+list(data_dir.glob('*.jpg'))
# image_list.sort(key=os.path.getctime)
# # image_list.reverse()
# dem = 0
# image_list = ['images/real9.jpg']
#
# for i in range(0,len(image_list)):
# 	print('========== begin ==========')
#
# 	imgpath = str(image_list[i])
# 	print('\tindex   ',i)
# 	print('\tpath   ',imgpath)
# 	img = cv2.imread(imgpath).copy()
# 	strngimg = img2string(img)
# 	made = run1(strngimg)
# 	print('\tmade   ',made)
# 	if '#' not in made:
# 		ans = run2(strngimg,'ABABAACDABDACABCDABDABCDABCDAB',10)
# 		print('\tans   ',ans[:50])
# 		if '999999' not in ans:
# 			dem=dem+1
# 			listres = ans.split('#####')
# 			print('\tsbd   ',listres[0])
# 			print('\tdiem   ',listres[1])
# 			imgg = string2img(listres[2])
# 			show(imgpath,imgg,900)
#
# 	print('==========  end  ==========')
# print('XONG')
# print('dem  ',dem,' / ',len(image_list))
# cv2.waitKey(0)
