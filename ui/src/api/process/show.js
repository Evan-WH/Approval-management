import request from '@/utils/request'

// 查询访客申请列表
export function listShow(query) {
    return request({
        url: '/process/show/list',
        method: 'get',
        params: query
    })
}

// 查询访客申请详细
export function getShow(id) {
    return request({
        url: '/process/show/' + id,
        method: 'get'
    })
}

// 新增访客申请
export function addShow(data) {
    return request({
        url: '/process/show',
        method: 'post',
        data: data
    })
}

// 修改访客申请
export function updateShow(data) {
    return request({
        url: '/process/show',
        method: 'put',
        data: data
    })
}

// 删除访客申请
export function delShow(id) {
    return request({
        url: '/process/show/' + id,
        method: 'delete'
    })
}