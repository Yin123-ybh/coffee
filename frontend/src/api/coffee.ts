import { getData, postData } from './http'

export interface Shop {
  id: number
  name: string
  type: string
  area: string
  address: string
  coverUrl: string
  score: number
  avgPrice: number
  heat: number
}

export interface ProductItem {
  id: number
  shopId: number
  name: string
  category: string
  description: string
  price: number
  stock: number
}

export interface Voucher {
  id: number
  shopId: number
  title: string
  subTitle: string
  payValue: number
  actualValue: number
}

export interface VoucherOrder {
  id: number
  userId: number
  voucherId: number
  shopId: number
  voucherTitle: string
  payAmount: number
  status: number
  orderNo: string
  createdAt: string
}

export function queryShops(keyword = '', type = '') {
  return getData<Shop[]>('/products/shops', { keyword, type })
}

export function queryItems(shopId?: number) {
  return getData<ProductItem[]>('/products/items', { shopId })
}

export function queryVouchers(shopId?: number) {
  return getData<Voucher[]>('/coupons/vouchers', { shopId })
}

export function createSeckillOrder(voucherId: number) {
  return postData<number>(`/seckill/vouchers/${voucherId}/orders`)
}

export function queryMyOrders() {
  return getData<VoucherOrder[]>('/orders/my')
}

export function payOrder(id: number) {
  return postData<void>(`/orders/${id}/pay`)
}

export function cancelOrder(id: number) {
  return postData<void>(`/orders/${id}/cancel`)
}

export function queryOverview() {
  return getData<Record<string, number>>('/stats/overview')
}
