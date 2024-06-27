// src/api/service.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; // 根据你的后端地址修改

export const getRooms = (page, size) => axios.get(`${API_BASE_URL}/rooms`, {
  params: {
    page: page - 1, // Spring Data JPA分页从0开始
    size: size
  }
});
// export const getRooms = () => axios.get(`${API_BASE_URL}/rooms`);
export const getUsers = () => axios.get(`${API_BASE_URL}/`);
// 更新用户信息
export const updateUser = (userData) => axios.put(`${API_BASE_URL}/${userData.id}`, userData);
// 删除用户
export const removeUser = (userId) => axios.delete(`${API_BASE_URL}/users/${userId}`);
// 禁用用户
export const disableUser = (userId) => axios.put(`${API_BASE_URL}/users/${userId}/disable`);

// 启用用户
export const enableUser = (userId) => axios.put(`${API_BASE_URL}/users/${userId}/enable`);
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
export const updateOrderStatus = (orderId, status) => axios.put(`${API_BASE_URL}/orders/${orderId}/status?status=${status}`);

// 新增房间
export const addRoom = (roomData) => axios.post(`${API_BASE_URL}/rooms`, roomData);
// 修改房间信息
export const updateRoom = (roomData) => axios.put(`${API_BASE_URL}/rooms/${roomData.id}`, roomData);
// 删除房间
export const removeRoom = (roomId) => axios.delete(`${API_BASE_URL}/rooms/${roomId}`);
