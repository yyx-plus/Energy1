const { post } = require('../../utils/request')

Page({
  data: {
    startLat: 31.230416, startLng: 121.473701,
    endLat: '', endLng: '', endName: '',
    remainRange: 100,
    result: null, loading: false
  },

  onLoad() {
    wx.getLocation({ 
      type: 'gcj02', 
      success: (res) => {
        this.setData({ startLat: res.latitude, startLng: res.longitude })
      },
      fail: (err) => {
        wx.showModal({
          title: '位置权限',
          content: '需要获取您的位置信息才能规划路径，请前往设置开启权限',
          confirmText: '去设置',
          success: (res) => {
            if (res.confirm) {
              wx.openSetting()
            }
          }
        })
      }
    })
  },

  onPullDownRefresh() {
    this.setData({ result: null })
    wx.stopPullDownRefresh()
  },

  onChooseEnd() {
    wx.chooseLocation({ 
      success: (res) => {
        this.setData({ endLat: res.latitude, endLng: res.longitude, endName: res.name })
      },
      fail: () => {
        wx.showToast({ title: '请选择目的地', icon: 'none' })
      }
    })
  },

  onRangeInput(e) {
    let val = parseInt(e.detail.value)
    if (isNaN(val)) val = 100
    if (val < 0) val = 0
    if (val > 1000) val = 1000
    this.setData({ remainRange: val })
  },

  async onPlan() {
    if (!this.data.endLat) { 
      wx.showToast({ title: '请选择目的地', icon: 'none' })
      return 
    }
    if (!this.data.startLat) {
      wx.showToast({ title: '请先获取位置', icon: 'none' })
      return
    }
    this.setData({ loading: true })
    try {
      const res = await post('/wx/route/plan', {
        startLat: this.data.startLat, startLng: this.data.startLng,
        endLat: this.data.endLat, endLng: this.data.endLng,
        remainRange: this.data.remainRange
      })
      this.setData({ result: res.data })
      if (res.data.stations && res.data.stations.length === 0) {
        wx.showToast({ title: '续航范围内暂无合适充电站', icon: 'none' })
      }
    } catch (e) {
      wx.showToast({ title: e.msg || '规划失败，请重试', icon: 'none' })
      console.error('路径规划失败', e)
    } finally {
      this.setData({ loading: false })
    }
  },

  goStation(e) {
    wx.navigateTo({ url: `/pages/station-detail/station-detail?id=${e.currentTarget.dataset.id}` })
  }
})
