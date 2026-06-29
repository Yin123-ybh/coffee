<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import {
  cancelOrder,
  createSeckillOrder,
  payOrder,
  queryItems,
  queryMyOrders,
  queryOverview,
  queryShops,
  queryVouchers,
  type ProductItem,
  type Shop,
  type Voucher,
  type VoucherOrder
} from './api/coffee'

const active = ref<'shops' | 'vouchers' | 'orders' | 'stats'>('shops')
const keyword = ref('')
const shopType = ref('')
const shops = ref<Shop[]>([])
const items = ref<ProductItem[]>([])
const vouchers = ref<Voucher[]>([])
const orders = ref<VoucherOrder[]>([])
const overview = ref<Record<string, number>>({})
const currentShopId = ref<number>()
const message = ref('系统运行正常')

const shopTypes = computed(() => Array.from(new Set(shops.value.map(item => item.type))))

function money(value: number) {
  return `¥${(value / 100).toFixed(value % 100 === 0 ? 0 : 2)}`
}

async function loadShops() {
  shops.value = await queryShops(keyword.value, shopType.value)
}

async function loadShopDetail(shop: Shop) {
  currentShopId.value = shop.id
  items.value = await queryItems(shop.id)
  vouchers.value = await queryVouchers(shop.id)
  active.value = 'vouchers'
}

async function seckill(voucher: Voucher) {
  const orderId = await createSeckillOrder(voucher.id)
  message.value = `秒杀请求已进入队列，订单号：${orderId}`
  await loadOrders()
}

async function loadOrders() {
  orders.value = await queryMyOrders()
}

async function pay(id: number) {
  await payOrder(id)
  message.value = '支付成功，统计服务会异步更新运营数据'
  await Promise.all([loadOrders(), loadStats()])
}

async function cancel(id: number) {
  await cancelOrder(id)
  message.value = '订单已取消'
  await loadOrders()
}

async function loadStats() {
  overview.value = await queryOverview()
}

onMounted(async () => {
  await Promise.all([loadShops(), loadOrders(), loadStats()])
})
</script>

<template>
  <main class="page">
    <header class="hero">
      <div>
        <p class="eyebrow">Spring Cloud Alibaba 微服务项目</p>
        <h1>分布式咖啡订购平台</h1>
        <p class="summary">前后端分离架构，覆盖商品浏览、优惠券秒杀、Kafka 异步下单、订单状态流转和运营统计。</p>
      </div>
      <section class="metrics">
        <div><span>门店</span><strong>{{ shops.length }}</strong></div>
        <div><span>订单</span><strong>{{ orders.length }}</strong></div>
        <div><span>成交</span><strong>{{ money(overview.payAmount || 0) }}</strong></div>
      </section>
    </header>

    <nav class="tabs">
      <button :class="{ active: active === 'shops' }" @click="active = 'shops'">门店商品</button>
      <button :class="{ active: active === 'vouchers' }" @click="active = 'vouchers'">优惠券秒杀</button>
      <button :class="{ active: active === 'orders' }" @click="active = 'orders'; loadOrders()">我的订单</button>
      <button :class="{ active: active === 'stats' }" @click="active = 'stats'; loadStats()">运营统计</button>
    </nav>

    <div class="notice">{{ message }}</div>

    <section v-if="active === 'shops'" class="panel">
      <div class="toolbar">
        <input v-model="keyword" placeholder="搜索门店、商圈或地址" @keyup.enter="loadShops" />
        <select v-model="shopType" @change="loadShops">
          <option value="">全部分类</option>
          <option v-for="type in shopTypes" :key="type" :value="type">{{ type }}</option>
        </select>
        <button @click="loadShops">查询</button>
      </div>
      <div class="grid">
        <article v-for="shop in shops" :key="shop.id" class="card" @click="loadShopDetail(shop)">
          <p class="muted">{{ shop.type }} · {{ shop.area }}</p>
          <h3>{{ shop.name }}</h3>
          <p>{{ shop.address }}</p>
          <footer><span>评分 {{ shop.score }}</span><span>人均 {{ money(shop.avgPrice * 100) }}</span></footer>
        </article>
      </div>
    </section>

    <section v-if="active === 'vouchers'" class="split">
      <div class="panel">
        <h2>商品列表</h2>
        <article v-for="item in items" :key="item.id" class="line-card">
          <div>
            <strong>{{ item.name }}</strong>
            <p>{{ item.description }}</p>
          </div>
          <span>{{ money(item.price) }}</span>
        </article>
      </div>
      <div class="panel">
        <h2>秒杀优惠券</h2>
        <article v-for="voucher in vouchers" :key="voucher.id" class="line-card">
          <div>
            <strong>{{ voucher.title }}</strong>
            <p>{{ voucher.subTitle }}，可抵 {{ money(voucher.actualValue) }}</p>
          </div>
          <button @click="seckill(voucher)">{{ money(voucher.payValue) }} 抢购</button>
        </article>
      </div>
    </section>

    <section v-if="active === 'orders'" class="panel">
      <h2>我的订单</h2>
      <article v-for="order in orders" :key="order.id" class="line-card">
        <div>
          <strong>{{ order.voucherTitle }}</strong>
          <p>{{ order.orderNo }} · 状态 {{ order.status }}</p>
        </div>
        <div class="actions">
          <button v-if="order.status === 1" @click="pay(order.id)">支付</button>
          <button v-if="order.status === 1" class="ghost" @click="cancel(order.id)">取消</button>
        </div>
      </article>
    </section>

    <section v-if="active === 'stats'" class="panel stats">
      <div><span>订单数</span><strong>{{ overview.orderCount || 0 }}</strong></div>
      <div><span>秒杀成功</span><strong>{{ overview.successCount || 0 }}</strong></div>
      <div><span>成交金额</span><strong>{{ money(overview.payAmount || 0) }}</strong></div>
    </section>
  </main>
</template>
