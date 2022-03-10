import request from '@/utils/request'

// 查询审批通过通知列表
export function listMessage(query) {
    return request({
        url: '/system/message/list',
        method: 'get',
        params: query
    })
}

// 查询审批通过通知详细
export function getMessage(id) {
    return request({
        url: '/system/message/' + id,
        method: 'get'
    })
}

// 新增审批通过通知
export function addMessage(data) {
    return request({
        url: '/system/message',
        method: 'post',
        data: data
    })
}

// 修改审批通过通知
export function updateMessage(data) {
    return request({
        url: '/system/message',
        method: 'put',
        data: data
    })
}

// 删除审批通过通知
export function delMessage(id) {
    return request({
        url: '/system/message/' + id,
        method: 'delete'
    })
}