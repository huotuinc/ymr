
/**
 * <p>在这个包中 定义与App交互的各个接口</p>
 * <p>to 服务端开发者：所有的实现者都是一个单例服务, {@link com.huotu.ymr.common.PublicParameterHolder}可以获得公共参数</p>
 * <p>to app:Output表示输出类型</p>
 * <p>
 * 火图加密算法：对传入的参数进行升序排序（sign除外），按照传参方式进行拼接
 * ，其中appsecret（483686ad1fe2bd8a02bbdca24e109953a4a96x测试密钥）参与排序
 * 例如
 * sign=a83686ad1fe2bd8a02bbdca24e109953a4a96b
 * timestamp=125487415444
 * version=3.0.0
 * operation=ymr2015huotu
 * imei=23ccccdded
 * token=b83686ad1fe2bd8a02bbdca24e109953a4a96c
 *
 * 拼接后
 * 483686ad1fe2bd8a02bbdca24e109953a4a96x23ccccddedymr2015huotu125487415444b83686ad1fe2bd8a02bbdca24e109953a4a96c3.0.0
 *
 * 其中sign、timestamp、version、operation、imei、token是公共参数
 * </p>
 */
package com.huotu.ymr.api;