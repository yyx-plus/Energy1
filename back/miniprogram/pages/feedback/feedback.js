const { post, get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    stationId: '', photos: [], baoxuiTypes: 1, baoxuiContent: '',
    typeOptions: ['设备故障', '油车占位', '其他'],
    myList: [], activeTab: 0
  },

  onShow() {
    this.loadMyList()
  },

  onPullDownRefresh() {
    this.loadMyList().finally(() => wx.stopPullDownRefresh())
  },

  async loadMyList() {
    wx.showLoading({ title: '加载中...', mask: true })
    try {
      const res = await get('/wx/feedback/myList', {}, false)
      this.setData({ myList: res.data || [] })
      wx.hideLoading()
    } catch (e) {
      wx.hideLoading()
      this.setData({ myList: [] })
    }
  },

  onTabChange(e) {
    this.setData({ activeTab: e.currentTarget.dataset.idx })
    if (e.currentTarget.dataset.idx === 1) this.loadMyList()
  },
  onStationInput(e) { this.setData({ stationId: e.detail.value }) },
  onContentInput(e) { this.setData({ baoxuiContent: e.detail.value }) },
  onTypeChange(e) { this.setData({ baoxuiTypes: parseInt(e.detail.value) + 1 }) },

  choosePhoto() {
    wx.chooseMedia({ count: 3 - this.data.photos.length, mediaType: ['image'],
      success: (res) => {
        const newPhotos = res.tempFiles.map(f => f.tempFilePath)
        this.setData({ photos: [...this.data.photos, ...newPhotos] })
      }
    })
  },

  removePhoto(e) {
    const photos = this.data.photos.filter((_, i) => i !== e.currentTarget.dataset.idx)
    this.setData({ photos })
  },

  async onSubmit() {
    if (!this.data.stationId) { wx.showToast({ title: '请输入充电站ID', icon: 'none' }); return }
    const photoUrl = this.data.photos[0] || ''
    wx.showLoading({ title: '提交中...', mask: true })
    try {
      await post('/wx/feedback/submit', {
        chongdianzhuangId: parseInt(this.data.stationId),
        baoxuiTypes: this.data.baoxuiTypes,
        baoxuiContent: this.data.baoxuiContent,
        baoxuiPhoto: photoUrl,
        baoxuiName: ['设备故障', '油车占位', '其他'][this.data.baoxuiTypes - 1]
      })
      wx.hideLoading()
      wx.showToast({ title: '提交成功，审核通过后获得积分', icon: 'success', duration: 2500 })
      this.setData({ stationId: '', photos: [], baoxuiContent: '', activeTab: 1 })
      setTimeout(() => this.loadMyList(), 1000)
    } catch (e) {
      wx.hideLoading()
      wx.showToast({ title: e.msg || '提交失败', icon: 'none' })
    }
  }
})
