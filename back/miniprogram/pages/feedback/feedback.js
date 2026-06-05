const { post, get } = require('../../utils/request')

Page({
  data: {
    stationId: '', photos: [], baoxuiTypes: 1, baoxuiContent: '',
    typeOptions: ['设备故障', '油车占位', '其他'],
    myList: [], activeTab: 0
  },

  onShow() { this.loadMyList() },

  onPullDownRefresh() {
    this.loadMyList().finally(() => wx.stopPullDownRefresh())
  },

  async loadMyList() {
    try {
      const res = await get('/wx/feedback/myList', {}, false)
      this.setData({ myList: res.data || [] })
    } catch (e) {}
  },

  onTabChange(e) { this.setData({ activeTab: e.currentTarget.dataset.idx }) },
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
    // 实际项目中需先上传图片获取url
    const photoUrl = this.data.photos[0] || ''
    try {
      await post('/wx/feedback/submit', {
        chongdianzhuangId: parseInt(this.data.stationId),
        baoxuiTypes: this.data.baoxuiTypes,
        baoxuiContent: this.data.baoxuiContent,
        baoxuiPhoto: photoUrl,
        baoxuiName: ['设备故障', '油车占位', '其他'][this.data.baoxuiTypes - 1]
      })
      wx.showToast({ title: '提交成功，审核通过后获得积分', icon: 'success', duration: 2500 })
      this.setData({ stationId: '', photos: [], baoxuiContent: '', activeTab: 1 })
      setTimeout(() => this.loadMyList(), 1000)
    } catch (e) {}
  }
})
