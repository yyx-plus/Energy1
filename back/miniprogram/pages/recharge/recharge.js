const { post } = require('../../utils/request')
Page({
  data: { amount: '', loading: false, presets: [50, 100, 200, 500] },
  onAmountInput(e) { this.setData({ amount: e.detail.value }) },
  selectPreset(e) { this.setData({ amount: String(e.currentTarget.dataset.val) }) },
  async onRecharge() {
    if (!this.data.amount || parseFloat(this.data.amount) <= 0) {
      wx.showToast({ title: '请输入充值金额', icon: 'none' }); return
    }
    this.setData({ loading: true })
    try {
      const res = await post('/wx/user/recharge', { amount: parseFloat(this.data.amount) })
      wx.showModal({ title: '充值成功', content: `余额：¥${res.data.newBalance}`, showCancel: false,
        success: () => wx.navigateBack() })
    } catch (e) {
    } finally { this.setData({ loading: false }) }
  }
})
