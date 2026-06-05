const { post } = require('../../utils/request')
const { formatCountdown } = require('../../utils/util')

Page({
  data: {
    orderId: null,
    order: null,
    phase: 'waiting', // waiting | charging | done
    countdown: 900,
    countdownText: '15:00',
    chargingSeconds: 0,
    chargingText: '00:00',
    mockKwh: 0,
    timer: null
  },

  async onLoad(options) {
    const orderId = options.orderId
    this.setData({ orderId })

    if (options.phase === 'charging') {
      // 从订单详情恢复充电中状态
      try {
        const { get } = require('../../utils/request')
        const res = await get(`/wx/order/${orderId}`)
        const order = res.data
        // 计算已充秒数
        let elapsed = 0
        if (order.startTime) {
          elapsed = Math.floor((Date.now() - new Date(order.startTime).getTime()) / 1000)
          if (elapsed < 0) elapsed = 0
        }
        const mockKwh = Math.round(elapsed / 60 * 7 * 100) / 100
        this.setData({
          order,
          phase: 'charging',
          chargingSeconds: elapsed,
          chargingText: require('../../utils/util').formatCountdown(elapsed),
          mockKwh
        })
        this.startCountdown()
      } catch (e) {
        // 获取失败也进入充电中界面
        this.setData({ phase: 'charging' })
        this.startCountdown()
      }
    } else {
      this.startCountdown()
    }
  },

  onUnload() {
    if (this.data.timer) clearInterval(this.data.timer)
  },

  startCountdown() {
    const timer = setInterval(() => {
      let { countdown, chargingSeconds, phase, mockKwh } = this.data
      if (phase === 'waiting') {
        countdown--
        if (countdown <= 0) {
          clearInterval(timer)
          wx.showModal({ title: '预约已过期', content: '请重新预约', showCancel: false,
            success: () => wx.navigateBack() })
          return
        }
        this.setData({ countdown, countdownText: formatCountdown(countdown) })
      } else if (phase === 'charging') {
        chargingSeconds++
        mockKwh = Math.round(chargingSeconds / 60 * 7 * 100) / 100 // 模拟7kW充电
        this.setData({
          chargingSeconds,
          chargingText: formatCountdown(chargingSeconds),
          mockKwh
        })
      }
    }, 1000)
    this.setData({ timer })
  },

  async onStartCharge() {
    try {
      await post('/wx/order/startCharge', { orderId: this.data.orderId })
      this.setData({ phase: 'charging' })
      wx.showToast({ title: '充电开始', icon: 'success' })
    } catch (e) {}
  },

  async onStopCharge() {
    wx.showModal({
      title: '确认结束充电？',
      content: `已充 ${this.data.mockKwh} kWh`,
      success: async (res) => {
        if (!res.confirm) return
        try {
          const result = await post('/wx/order/stopCharge', {
            orderId: this.data.orderId,
            chargeKwh: this.data.mockKwh
          })
          this.setData({ phase: 'done' })
          wx.redirectTo({ url: `/pages/pay/pay?orderId=${this.data.orderId}` })
        } catch (e) {}
      }
    })
  }
})
