const { post } = require('../../utils/request')
const app = getApp()

Page({
  data: {
    username: '',
    password: '',
    loading: false
  },

  onUsernameInput(e) { this.setData({ username: e.detail.value }) },
  onPasswordInput(e) { this.setData({ password: e.detail.value }) },

  async onLogin() {
    const { username, password } = this.data
    if (!username || !password) {
      wx.showToast({ title: '请输入账号和密码', icon: 'none' })
      return
    }
    this.setData({ loading: true })
    try {
      const res = await post('/wx/auth/loginByPhone', { username, password }, false)
      app.globalData.token = res.token
      app.globalData.userInfo = res.userInfo
      wx.setStorageSync('token', res.token)
      wx.setStorageSync('userInfo', res.userInfo)
      wx.switchTab({ url: '/pages/index/index' })
    } catch (e) {
      // 错误已在request.js中toast
    } finally {
      this.setData({ loading: false })
    }
  },

  // 微信一键登录（模拟，实际需要后端换取openid）
  async onWxLogin() {
    wx.getUserProfile({
      desc: '用于完善用户信息',
      success: async (profileRes) => {
        // 模拟openid（实际应用中需要wx.login获取code，后端换取openid）
        const mockOpenid = 'mock_' + Date.now()
        try {
          const res = await post('/wx/auth/login', {
            openid: mockOpenid,
            nickName: profileRes.userInfo.nickName,
            avatarUrl: profileRes.userInfo.avatarUrl
          }, false)
          app.globalData.token = res.token
          app.globalData.userInfo = res.userInfo
          wx.setStorageSync('token', res.token)
          wx.setStorageSync('userInfo', res.userInfo)
          wx.switchTab({ url: '/pages/index/index' })
        } catch (e) {}
      }
    })
  },

  goRegister() {
    wx.navigateTo({ url: '/pages/login/register' })
  }
})
