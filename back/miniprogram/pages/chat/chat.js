const { get, post } = require('../../utils/request')

Page({
  data: { list: [], inputVal: '', submitting: false, lastId: '' },
  _pollTimer: null,

  onShow() {
    this.loadList()
    // 每10秒自动刷新，及时显示客服回复
    this._pollTimer = setInterval(() => this.loadList(), 10000)
  },

  onHide() {
    if (this._pollTimer) { clearInterval(this._pollTimer); this._pollTimer = null }
  },

  onUnload() {
    if (this._pollTimer) { clearInterval(this._pollTimer); this._pollTimer = null }
  },

  onPullDownRefresh() {
    this.loadList().finally(() => wx.stopPullDownRefresh())
  },

  async loadList() {
    try {
      const res = await get('/wx/chat/list', {}, false)
      const list = res.data || []
      const lastId = list.length > 0 ? 'anchor-' + list[list.length - 1].id : ''
      this.setData({ list, lastId })
    } catch (e) {}
  },

  onInput(e) { this.setData({ inputVal: e.detail.value }) },

  async onSubmit() {
    const content = this.data.inputVal.trim()
    if (!content) { wx.showToast({ title: '请输入留言内容', icon: 'none' }); return }
    this.setData({ submitting: true })
    try {
      await post('/wx/chat/submit', { chatIssue: content })
      this.setData({ inputVal: '' })
      wx.showToast({ title: '留言已提交', icon: 'success' })
      await this.loadList()
    } catch (e) {
    } finally {
      this.setData({ submitting: false })
    }
  }
})
