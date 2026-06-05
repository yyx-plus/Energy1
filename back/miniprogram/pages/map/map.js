const { get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    latitude: 31.230416,
    longitude: 121.473701,
    markers: [],
    stations: [],
    filters: { chongdianzhuangTypes: null, isFreeParking: null, keyword: '' },
    stationTypes: [], // 充电桩类型字典
    selectedStation: null,
    showDetail: false,
    scale: 13
  },

  onLoad() {
    // 从全局字典加载充电桩类型
    const app = getApp()
    const stationTypes = app.globalData.stationTypes
    if (stationTypes && stationTypes.length > 0) {
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

  async loadNearbyList() {
    const { latitude, longitude, filters } = this.data
    // 过滤掉 null 和空字符串，避免后端400
    const params = { latitude, longitude, radius: 10 }
    if (filters.chongdianzhuangTypes !== null) params.chongdianzhuangTypes = filters.chongdianzhuangTypes
    if (filters.isFreeParking !== null) params.isFreeParking = filters.isFreeParking
    if (filters.keyword && filters.keyword.trim() !== '') params.keyword = filters.keyword
    try {
      const res = await get('/wx/station/list', params, false)
      this.setData({ stations: (res.data && res.data.list) || [] })
    } catch (e) {}
  },

  onMarkerTap(e) {
    const station = this.data.stations.find(s => s.id === e.markerId)
    if (station) this.setData({ selectedStation: station, showDetail: true })
  },

  onFilterChange(e) {
    const { key, val } = e.currentTarget.dataset
    const current = this.data.filters[key]
    const intVal = parseInt(val)
    // 再次点击同一个标签则取消
    const filters = { ...this.data.filters, [key]: current === intVal ? null : intVal }
    this.setData({ filters })
    this.loadNearbyList()
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
