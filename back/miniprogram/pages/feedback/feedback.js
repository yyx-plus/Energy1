const { post, get } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    stationId: '', photos: [], baoxuiTypes: 1, baoxuiContent: '',
    typeOptions: ['设备故障', '油车占位', '其他'],
    myList: [], activeTab: 0,
    isLoggedIn: false
  },

  onShow() {
    // 检查登录状态
    this.setData({ isLoggedIn: !!app.globalData.token })
    if (this.data.isLoggedIn) {
      this.loadMyList()
    }
  },

  onPullDownRefresh() {
    if (this.data.isLoggedIn) {
      this.loadMyList().finally(() => wx.stopPullDownRefresh())
    } else {
      wx.stopPullDownRefresh()
    }
  },

  async loadMyList() {
    if (!this.data.isLoggedIn) {
      this.setData({ myList: [] })
      return
    }
    wx.showLoading({ title: '加载中...', mask: true })
    try {
      const res = await get('/wx/feedback/myList', {}, false)
      this.setData({ myList: res.data || [] })
      wx.hideLoading()
    } catch (e) {
      wx.hideLoading()
      if (e.code === 401) {
        wx.showToast({ title: '请先登录', icon: 'none' })
        this.setData({ myList: [], isLoggedIn: false })
      }
    }
  },

  onTabChange(e) {
    const idx = e.currentTarget.dataset.idx
    if (idx === 1 && !this.data.isLoggedIn) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    this.setData({ activeTab: idx })
    if (idx === 1) this.loadMyList()
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
    if (!this.data.isLoggedIn) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    if (!this.data.stationId) { wx.showToast({ title: '请输入充电站ID', icon: 'none' }); return }
    // 实际项目中需先上传图片获取url
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
      if (e.code === 401) {
        wx.showToast({ title: '请先登录', icon: 'none' })
      } else {
        wx.showToast({ title: e.msg || '提交失败', icon: 'none' })
      }
    }
  },

  goLogin() {
    wx.navigateTo({ url: '/pages/login/login' })
  }
})
