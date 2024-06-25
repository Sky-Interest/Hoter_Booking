// src/api/service.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // 根据你的后端地址修改

export const getRooms = () => axios.get(`${API_BASE_URL}/rooms`);
export const getUsers = () => axios.get(`${API_BASE_URL}/users`);
export const getOrders = () => axios.get(`${API_BASE_URL}/orders`);
export const createOrder = (order) => axios.post(`${API_BASE_URL}/orders`, order);
// 登录
export const loginUser = (userData) => axios.post(`${API_BASE_URL}/login`, userData);

// 注册
export const createUser = (userData) => axios.post(`${API_BASE_URL}/register`, userData);
// 在service.js中添加
export const getAvailableRooms = () => axios.get(`${API_BASE_URL}/rooms/available`);
// 获取订单详情
export const getOrderDetail = (orderId) => axios.get(`${API_BASE_URL}/orders/${orderId}`);

// 提交支付
export const submitPayment = (paymentData) => axios.post(`${API_BASE_URL}/payments`, paymentData);

