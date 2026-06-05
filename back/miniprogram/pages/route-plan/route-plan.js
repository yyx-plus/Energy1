const { post } = require('../../utils/request')

Page({
  data: {
    startLat: 31.230416, startLng: 121.473701,
    endLat: '', endLng: '', endName: '',
    remainRange: 100,
    result: null, loading: false
  },

  onLoad() {
    wx.getLocation({ type: 'gcj02', success: (res) => {
      this.setData({ startLat: res.latitude, startLng: res.longitude })
    }})
  },

  onPullDownRefresh() {
    this.setData({ result: null })
    wx.stopPullDownRefresh()
  },

  onChooseEnd() {
    wx.chooseLocation({ success: (res) => {
      this.setData({ endLat: res.latitude, endLng: res.longitude, endName: res.name })
    }})
  },

  onRangeInput(e) { this.setData({ remainRange: e.detail.value }) },

  async onPlan() {
    if (!this.data.endLat) { wx.showToast({ title: '请选择目的地', icon: 'none' }); return }
    this.setData({ loading: true })
    try {
      const res = await post('/wx/route/plan', {
        startLat: this.data.startLat, startLng: this.data.startLng,
        endLat: this.data.endLat, endLng: this.data.endLng,
        remainRange: this.data.remainRange
      })
      this.setData({ result: res.data })
    } catch (e) {
    } finally {
      this.setData({ loading: false })
    }
  },

  goStation(e) {
    wx.navigateTo({ url: `/pages/station-detail/station-detail?id=${e.currentTarget.dataset.id}` })
  }
})
