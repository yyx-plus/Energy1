// utils/request.js - 统一请求封装
const app = getApp()

/**
 * 封装 wx.request，自动带 Token，统一错误处理
 * @param {string} url 接口路径（不含baseUrl）
 * @param {string} method 请求方法
 * @param {object} data 请求数据
 * @param {boolean} showLoading 是否显示loading
 */
function request(url, method = 'GET', data = {}, showLoading = true) {
  return new Promise((resolve, reject) => {
    if (showLoading) wx.showLoading({ title: '加载中...', mask: true })

    wx.request({
      url: app.globalData.baseUrl + url,
      method,
      data,
      header: {
        'Content-Type': 'application/json',
        'Token': app.globalData.token || ''
      },
      success(res) {
        if (showLoading) wx.hideLoading()
        const { data: resData } = res
        if (res.statusCode === 200) {
          if (resData.code === 0 || resData.code === undefined) {
            resolve(resData)
          } else if (resData.code === 401) {
            wx.showToast({ title: '请先登录', icon: 'none' })
            app.logout()
            reject(resData)
          } else {
            wx.showToast({ title: resData.msg || '请求失败', icon: 'none' })
            reject(resData)
          }
        } else {
          wx.showToast({ title: '网络错误', icon: 'none' })
          reject(res)
        }
      },
      fail(err) {
        if (showLoading) wx.hideLoading()
        wx.showToast({ title: '网络连接失败', icon: 'none' })
        reject(err)
      }
    })
  })
}

const get = (url, data, showLoading) => request(url, 'GET', data, showLoading)
const post = (url, data, showLoading) => request(url, 'POST', data, showLoading)
const put = (url, data, showLoading) => request(url, 'PUT', data, showLoading)
const del = (url, data, showLoading) => request(url, 'DELETE', data, showLoading)

module.exports = { request, get, post, put, del }
