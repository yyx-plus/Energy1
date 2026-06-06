const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    latitude: 31.230416,
    longitude: 121.473701,
    markers: [],
    stations: [],
    filters: { chongdianzhuangTypes: null, isFreeParking: null, isFastCharge: null, keyword: '' }, //筛选条件
    stationTypes: [], // 充电桩类型字典
    selectedStation: null,
    showDetail: false,
    scale: 13
  },

  //界面加载时获取类型字典，生命周期钩子函数，界面加载自动执行（only）
  onLoad() {
    // 从全局字典加载充电桩类型
    const app = getApp()
    const stationTypes = app.globalData.stationTypes //读取数据
    if (stationTypes && stationTypes.length > 0) { //异步处理
      this.setData({ stationTypes })
    } else {
      // 字典还未加载完，稍等后重试
      setTimeout(() => {
        this.setData({ stationTypes: getApp().globalData.stationTypes || [] })
      }, 800)
    }
    this.getLocation()
  },

  onPullDownRefresh() {
    this.loadMapData()
    this.loadNearbyList().finally(() => wx.stopPullDownRefresh())
  },

  getLocation() {
    wx.getLocation({
      type: 'gcj02',
      success: (res) => {
        this.setData({ latitude: res.latitude, longitude: res.longitude })
        this.loadMapData()
        this.loadNearbyList()
      },
      fail: () => {
        this.loadMapData()
        this.loadNearbyList()
      }
    })
  },

  async loadMapData() {
    try {
      const res = await get('/wx/station/map', {}, false)
      const markers = (res.data || []).map(s => ({
        id: s.id,
        latitude: s.latitude,
        longitude: s.longitude,
        title: s.name,
        // 使用微信内置图标，避免本地图片不存在导致marker不显示
        width: 32, height: 32,
        label: {
          content: s.freeGunCount > 0 ? `空闲${s.freeGunCount}` : '占用',
          color: s.freeGunCount > 0 ? '#07c160' : '#fa5151',
          fontSize: 11, borderRadius: 4,
          bgColor: '#fff', padding: 3,
          anchorX: 0, anchorY: -10
        },
        callout: {
          content: `${s.name}\n空闲${s.freeGunCount}/${s.totalGunCount}`,
          color: '#333', fontSize: 12, borderRadius: 8,
          bgColor: '#fff', padding: 8, display: 'BYCLICK'
        }
      }))
      this.setData({ markers })
    } catch (e) {}
  },
  //查询功能
  async loadNearbyList() {
    const { latitude, longitude, filters } = this.data
    // 过滤掉 null 和空字符串，避免后端400
    const params = { latitude, longitude, radius: 10 }
    if (filters.chongdianzhuangTypes !== null) params.chongdianzhuangTypes = filters.chongdianzhuangTypes
    if (filters.isFreeParking !== null) params.isFreeParking = filters.isFreeParking
    if (filters.isFastCharge !== null) params.isFastCharge = filters.isFastCharge
    // 修复：正确判断 keyword 是否有效（不为 null/undefined 且 trim 后不为空）
    if (filters.keyword != null && filters.keyword.trim() !== '') {
      params.keyword = filters.keyword.trim()
    }
    //发送请求
    wx.showLoading({ title: '搜索中...', mask: true })
    try {
      const res = await get('/wx/station/list', params, false)  //封装好的get方法
      this.setData({ stations: (res.data && res.data.list) || [] }) //接受后端返回来的数据
      wx.hideLoading()
      if (this.data.stations.length === 0) {
        wx.showToast({ title: '未找到匹配的充电桩', icon: 'none' })
      }
    } catch (e) {
      wx.hideLoading()
      wx.showToast({ title: '搜索失败，请重试', icon: 'none' })
    }
  },

  onMarkerTap(e) {
    const station = this.data.stations.find(s => s.id === e.markerId)
    if (station) this.setData({ selectedStation: station, showDetail: true })
  },
  //用户筛选标签
  onFilterChange(e) {
    const { key, val } = e.currentTarget.dataset  // key="chongdianzhuangTypes", val="3"
    const current = this.data.filters[key]
    const intVal = parseInt(val)           // 字符串"3" → 整数3
    // 再次点击同一个标签则取消
    const filters = { ...this.data.filters, [key]: current === intVal ? null : intVal }
    this.setData({ filters })
    this.loadNearbyList()  //触发查询
    this.loadMapData()
  },

  onKeywordInput(e) {
    this.setData({ 'filters.keyword': e.detail.value })
  },

  onSearch() {
    this.loadNearbyList()
    this.loadMapData()
  },

  goDetail(e) {
    const id = e.currentTarget.dataset.id || this.data.selectedStation?.id
    wx.navigateTo({ url: `/pages/station-detail/station-detail?id=${id}` })
  },

  closeDetail() { this.setData({ showDetail: false }) }
})
