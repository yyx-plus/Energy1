// app.js
App({
  globalData: {
    userInfo: null,
    token: null,
    baseUrl: 'http://127.0.0.1:8080/xinnengyuanchongdainxitong',
    stationTypes: [] // 充电桩类型字典，动态从后端加载
  },

  onLaunch() {
    const token = wx.getStorageSync('token')
    const userInfo = wx.getStorageSync('userInfo')
    if (token) {
      this.globalData.token = token
      this.globalData.userInfo = userInfo
    }
    this.loadDictionary()
  },

  // 加载字典数据
  loadDictionary() {
    wx.request({
      url: this.globalData.baseUrl + '/dictionary/wx/list',
      data: { dicCode: 'chongdianzhuang_types' },
      success: (res) => {
        if (res.data && res.data.code === 0) {
          this.globalData.stationTypes = res.data.data || []
        }
      }
    })
  },

  // 检查登录状态，未登录跳转登录页
  checkLogin() {
    if (!this.globalData.token) {
      wx.navigateTo({ url: '/pages/login/login' })
      return false
    }
    return true
  },

  // 退出登录
  logout() {
    this.globalData.token = null
    this.globalData.userInfo = null
    wx.removeStorageSync('token')
    wx.removeStorageSync('userInfo')
    wx.reLaunch({ url: '/pages/login/login' })
  }
})
