import request from '@/utils/request'
// 查询部门列表
export function listDept(query) {
    return request({
        url: '/system/dept/list',
        method: 'get',
        params: query
    })
}
// 查询访客申请列表
export function listProcess(query) {
    return request({
        url: '/system/process/list',
        method: 'get',
        params: query
    })
}

// 查询访客申请详细
export function getProcess(id) {
    return request({
        url: '/system/process/' + id,
        method: 'get'
    })
}

// 新增访客申请
export function addProcess(data) {
    return request({
        url: '/system/process',
        method: 'post',
        data: data
    })
}

// 修改访客申请
export function updateProcess(data) {
    return request({
        url: '/system/process',
        method: 'put',
        data: data
    })
}

// 删除访客申请
export function delProcess(id) {
    return request({
        url: '/system/process/' + id,
        method: 'delete'
    })
}