# coding: utf-8
# get data for views
import pandas as pd

listChinese = {"beijing":"北京","shanghai":"上海","tianjin":"天津","chongqing":"重庆","haerbin":"哈尔滨","changchun":"长春",
               "shenyang":"沈阳","huhehaote":"呼和浩特","shijiazhuang":"石家庄","wulumuqi":"乌鲁木齐","lanzhou":"兰州","xining":"西宁",
               "xian":"西安","yinchuan":"银川","zhengzhou":"郑州","jinan":"济南","taiyuan":"太原","hefei":"合肥",
               "wuhan":"武汉","nanjing":"南京","chengdu":"成都","guiyang":"贵阳","kunming":"昆明","nanning":"南宁",
               "lasa":"拉萨","hangzhou":"杭州","nanchang":"南昌","guangzhou":"广州","fuzhou":"福州","haikou":"海口","changsha":"长沙"}

strFileOpenGeneral = "d:/data/test/year/allYears/11-totalIndexClassify.csv"
strFileOpenGeneralA = "d:/data/test/year/allYears/5-totalIndexAllA.csv"
strFileOpenGeneralW = "d:/data/test/year/allYears/5-totalIndexAllW.csv"

strFileOpenCity2014 = "d:/data/test/year/airWeather/6-totalIndexAW2014Ranked.csv"
strFileOpenCity2015 = "d:/data/test/year/airWeather/6-totalIndexAW2015Ranked.csv"
strFileOpenCity2016 = "d:/data/test/year/airWeather/6-totalIndexAW2016Ranked.csv"

strFileOpenCityAir2014 = "d:/data/test/year/air/air9/totalIndex2014.csv"
strFileOpenCityAir2015 = "d:/data/test/year/air/air9/totalIndex2015.csv"
strFileOpenCityAir2016 = "d:/data/test/year/air/air9/totalIndex2016.csv"

strFileOpenCityWeather2014 = "d:/data/test/year/weather/weather10/totalIndex2014.csv"
strFileOpenCityWeather2015 = "d:/data/test/year/weather/weather10/totalIndex2015.csv"
strFileOpenCityWeather2016 = "d:/data/test/year/weather/weather10/totalIndex2016.csv"

strFileOpenTreeClass = "d:/data/test/year/newClassify/2-addClass.csv"

# 获取总三年的totalIndex
def getTotalIndexGeneral(): 
    fa = pd.read_csv(strFileOpenGeneralA)
    fw= pd.read_csv(strFileOpenGeneralW)
    ft = pd.read_csv(strFileOpenGeneral)
    
        
    fa = fa.sort_values(["totalIndex"],ascending=False)
    aList = fa['totalIndex'].values.tolist()
    fw = fw.sort_values(["totalIndex"],ascending=False)
    wList = fw['totalIndex'].values.tolist()
    ft = ft.sort_values(["totalIndex"],ascending=False)    
    tList = ft['totalIndex'].values.tolist()
    # 需要将series转为array再转为list，才符合json格式
    
    listEnglish = ft['city'].values.tolist()
    cityList = []
    for i in range (0, len(listEnglish)):
        cityList.append(listChinese[listEnglish[i]])


    data = {'categories':cityList, 'dataA':aList, 'dataW':wList, 'dataT':tList}
    return data
    
    
# 获取总3年的awIndex    
def getAWIndexGeneral():
    f = pd.read_csv(strFileOpenGeneral)
    f = f.sort_values(["totalIndex"],ascending=False)
    listEnglish = f['city'].values.tolist()
    cityList = []
    for i in range (0, len(listEnglish)):
        cityList.append(listChinese[listEnglish[i]])
    data = {'categories':cityList, 'dataA':f['indexA'].values.tolist(), 'dataW':f['indexW'].values.tolist(), 'dataT':f['totalIndex'].values.tolist()}
    # 需要将series转为array再转为list，才符合json格式
    return data
    

# 获取总的地图数据
def getMapGeneral():
    fa = pd.read_csv(strFileOpenGeneralA)
    fw= pd.read_csv(strFileOpenGeneralW)
    ft = pd.read_csv(strFileOpenGeneral)
    listEnglish = ft['city'].values.tolist()
    cityList = []
    for i in range (0, len(listEnglish)):
        cityList.append(listChinese[listEnglish[i]])
    aList = fa['totalIndex'].values.tolist()
    wList = fw['totalIndex'].values.tolist()
    tList = ft['totalIndex'].values.tolist()
    
    totalData = []
    airData = []
    weatherData = []
    for i in range (0, len(cityList)):
        airData.append({'name':cityList[i],'value':aList[i]*100})
        weatherData.append({'name':cityList[i],'value':wList[i]*100})
        totalData.append({'name':cityList[i],'value':tList[i]*100})
    return {'totalData':totalData,'airData':airData,'weatherData':weatherData}


# 获取城市变化数据
def getChangeCity(cityName):
    f = pd.read_csv(strFileOpenCity2014) # 获取一年中的3个index
    list2014 = []
    for row in f.itertuples(index=False): # 遍历每一行
        if row[0] == cityName: 
            list2014 = row
            break
    
    f = pd.read_csv(strFileOpenCity2015)
    list2015 = []
    for row in f.itertuples(index=False): # 遍历每一行
        if row[0] == cityName: 
            list2015 = row
            break
    
    f = pd.read_csv(strFileOpenCity2016)
    list2016 = []
    for row in f.itertuples(index=False): # 遍历每一行
        if row[0] == cityName: 
            list2016 = row
            break
    
    a2014 = -int(list2014[4]) # 将numpy.int 转化为int， 才能传json
    a2015 = -int(list2015[4]) # 为了图像显示，取负数
    a2016 = -int(list2016[4])
    w2014 = -int(list2014[5])
    w2015 = -int(list2015[5]) 
    w2016 = -int(list2016[5])
    t2014 = -int(list2014[6])
    t2015 = -int(list2015[6])
    t2016 = -int(list2016[6])

    return {'dataA': [a2014,a2015,a2016], 'dataW': [w2014,w2015,w2016],
            'dataT':[t2014,t2015,t2016]} # 组合成每个index 3年的变化



# 获取详细空气指数
def getAirCity(cityName):
    # 2014
    f = pd.read_csv(strFileOpenCityAir2014)
    namesList = f.columns
    allList = {}
    itemList = []
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
        if name == 'city': continue
        itemList.append(name)
    #print allList
    
    list2014 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    
    for name in namesList:
        if name == 'city': continue
        list2014.append(allList[name][position])
        

    # 2015
    f = pd.read_csv(strFileOpenCityAir2015)
    namesList = f.columns
    allList = {}
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
    #print allList
    
    list2015 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    
    for name in namesList:
        if name == 'city': continue
        list2015.append(allList[name][position])
        

    # 2016
    f = pd.read_csv(strFileOpenCityAir2016)
    namesList = f.columns
    allList = {}
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
    #print allList
    list2016 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    for name in namesList:
        if name == 'city': continue
        list2016.append(allList[name][position])
        
    
    #print f['rankA'].values.tolist()
    data = {'categories':itemList, 'data2014':list2014,'data2015':list2015,'data2016':list2016}
    #print data
    return data


# 获取详细天气指数
def getWeatherCity(cityName):
    # 2014
    f = pd.read_csv(strFileOpenCityWeather2014)
    namesList = f.columns
    allList = {}
    itemList = []
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
        if name == 'city': continue
        itemList.append(name)
    #print allList
    
    list2014 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    
    for name in namesList:
        if name == 'city': continue
        list2014.append(allList[name][position])
        

    # 2015
    f = pd.read_csv(strFileOpenCityWeather2015)
    namesList = f.columns
    allList = {}
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
    #print allList
    
    list2015 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    
    for name in namesList:
        if name == 'city': continue
        list2015.append(allList[name][position])
        

    # 2016
    f = pd.read_csv(strFileOpenCityWeather2016)
    namesList = f.columns
    allList = {}
    for name in namesList:
        allList[name] = f[name].values.tolist() # 全部数据存入字典
    #print allList
    list2016 = []
    position = 0
    for i in range(0, len(allList['city'])): # 找到城市所在位置
        if allList['city'][i] == cityName:
            position = i
            break
    #print position
    for name in namesList:
        if name == 'city': continue
        list2016.append(allList[name][position])
        
    
    #print f['rankA'].values.tolist()
    data = {'categories':itemList, 'data2014':list2014,'data2015':list2015,'data2016':list2016}
    #print data
    return data


# 获取分类地图数据
def getMapTree():
    f = pd.read_csv(strFileOpenTreeClass)
    listEnglish = f['city'].values.tolist()
    cityList = []
    for i in range (0, len(listEnglish)):
        cityList.append(listChinese[listEnglish[i]])
         
    aList = f['classifyA'].values.tolist()
    wList = f['classifyW'].values.tolist()
    
    airData = []
    weatherData = []
    for i in range (0, len(cityList)):
        airData.append({'name':cityList[i],'value':aList[i]})
        weatherData.append({'name':cityList[i],'value':wList[i]})
    return {'airData':airData,'weatherData':weatherData}
