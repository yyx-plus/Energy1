<template>
    <div class="main-content">
        <!-- 增强查询区域 -->
        <div class="query-section">
            <el-row :gutter="20" class="filter-row">
                <el-col :span="24">
                    <el-card class="filter-card">
                        <div slot="header" class="card-header">
                            <span>充电桩智能查询</span>
                        </div>
                        <el-form :inline="true" :model="searchForm" class="filter-form">
                            <!-- 基础筛选 -->
                            <el-form-item label="充电桩名称">
                                <el-input
                                    v-model="searchForm.chongdianzhuangName"
                                    placeholder="请输入充电桩名称"
                                    clearable
                                    prefix-icon="el-icon-search"
                                    @keyup.enter.native="search()">
                                </el-input>
                            </el-form-item>

                            <el-form-item label="充电桩类型">
                                <el-select v-model="searchForm.chongdianzhuangTypes" placeholder="请选择类型" clearable>
                                    <el-option label="全部" value=""></el-option>
                                    <el-option
                                        v-for="(item,index) in chongdianzhuangTypesSelectSearch"
                                        :key="index"
                                        :label="item.indexName"
                                        :value="item.codeIndex">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <el-form-item label="实时状态">
                                <el-select v-model="searchForm.chongdianzhuangZhuangtaiTypes" placeholder="请选择状态" clearable>
                                    <el-option label="全部" value=""></el-option>
                                    <el-option
                                        v-for="(item,index) in chongdianzhuangZhuangtaiTypesList"
                                        :key="index"
                                        :label="item.indexName"
                                        :value="item.codeIndex">
                                    </el-option>
                                </el-select>
                            </el-form-item>

                            <!-- 高级筛选 -->
                            <el-collapse-transition>
                                <div v-show="showAdvanced">
                                    <el-form-item label="价格区间">
                                        <el-input-number
                                            v-model="searchForm.chongdianzhuangNewMoneyStart"
                                            controls-position="right"
                                            :min="0"
                                            placeholder="最低价"
                                            style="width: 120px;">
                                        </el-input-number>
                                        <span class="separator">-</span>
                                        <el-input-number
                                            v-model="searchForm.chongdianzhuangNewMoneyEnd"
                                            controls-position="right"
                                            :min="0"
                                            placeholder="最高价"
                                            style="width: 120px;">
                                        </el-input-number>
                                    </el-form-item>

                                    <el-form-item label="可用时长">
                                        <el-input-number
                                            v-model="searchForm.chongdianzhuangKucunNumberStart"
                                            controls-position="right"
                                            :min="0"
                                            placeholder="最少"
                                            style="width: 120px;">
                                        </el-input-number>
                                        <span class="separator">-</span>
                                        <el-input-number
                                            v-model="searchForm.chongdianzhuangKucunNumberEnd"
                                            controls-position="right"
                                            :min="0"
                                            placeholder="最多"
                                            style="width: 120px;">
                                        </el-input-number>
                                    </el-form-item>

                                    <el-form-item label="搜索范围">
                                        <el-select v-model="searchForm.searchRadius" placeholder="请选择范围" style="width: 150px;">
                                            <el-option label="5公里内" :value="5"></el-option>
                                            <el-option label="10公里内" :value="10"></el-option>
                                            <el-option label="20公里内" :value="20"></el-option>
                                            <el-option label="50公里内" :value="50"></el-option>
                                            <el-option label="不限" :value="null"></el-option>
                                        </el-select>
                                    </el-form-item>

                                    <el-form-item label="排序方式">
                                        <el-select v-model="searchForm.sort" placeholder="请选择排序">
                                            <el-option label="距离优先" value="distance"></el-option>
                                            <el-option label="价格优先" value="chongdianzhuang_new_money"></el-option>
                                            <el-option label="可用时长" value="chongdianzhuang_kucun_number"></el-option>
                                            <el-option label="默认排序" value="id"></el-option>
                                        </el-select>
                                    </el-form-item>

                                    <el-form-item label="排序">
                                        <el-select v-model="searchForm.order" placeholder="请选择">
                                            <el-option label="升序" value="asc"></el-option>
                                            <el-option label="降序" value="desc"></el-option>
                                        </el-select>
                                    </el-form-item>
                                </div>
                            </el-collapse-transition>

                            <el-form-item>
                                <el-button type="primary" @click="search()" icon="el-icon-search">查询</el-button>
                                <el-button @click="resetSearchForm()" icon="el-icon-refresh">重置</el-button>
                                <el-button type="text" @click="showAdvanced = !showAdvanced">
                                    {{ showAdvanced ? '收起高级筛选' : '展开高级筛选' }}
                                    <i :class="showAdvanced ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
                                </el-button>
                            </el-form-item>
                        </el-form>

                        <!-- 快速筛选标签 -->
                        <div class="quick-filters">
                            <el-checkbox v-model="searchForm.onlyAvailable" @change="search()">只看可用</el-checkbox>
                            <el-tag
                                v-for="tag in quickFilterTags"
                                :key="tag.label"
                                :type="tag.type"
                                class="filter-tag"
                                @click="handleQuickFilter(tag)">
                                {{ tag.label }}
                            </el-tag>
                        </div>

                        <!-- 位置获取按钮 -->
                        <div class="location-section">
                            <el-button
                                type="warning"
                                icon="el-icon-location"
                                @click="getCurrentLocation()"
                                :loading="locationLoading">
                                {{ locationLoading ? '获取位置中...' : '获取我的位置' }}
                            </el-button>
                            <span v-if="currentLocation" class="location-info">
                                当前坐标: {{ currentLocation.latitude.toFixed(4) }}, {{ currentLocation.longitude.toFixed(4) }}
                            </span>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>

        <!-- 查询结果 -->
        <div class="results-section">
            <el-row :gutter="20">
                <!-- 统计信息 -->
                <el-col :span="24">
                    <el-card class="stats-card">
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <div class="stat-item">
                                    <div class="stat-label">总充电桩数</div>
                                    <div class="stat-value">{{ statistics.total }}</div>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="stat-item">
                                    <div class="stat-label">可用充电桩</div>
                                    <div class="stat-value available">{{ statistics.available }}</div>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="stat-item">
                                    <div class="stat-label">空闲充电桩</div>
                                    <div class="stat-value idle">{{ statistics.idle }}</div>
                                </div>
                            </el-col>
                            <el-col :span="6">
                                <div class="stat-item">
                                    <div class="stat-label">平均价格</div>
                                    <div class="stat-value price">¥{{ statistics.avgPrice }}</div>
                                </div>
                            </el-col>
                        </el-row>
                    </el-card>
                </el-col>
            </el-row>

            <!-- 充电桩列表 -->
            <el-row :gutter="20" class="charging-list">
                <el-col
                    v-for="item in dataList"
                    :key="item.id"
                    :xs="24"
                    :sm="12"
                    :md="8"
                    :lg="6"
                    class="charging-card-col">
                    <el-card class="charging-card" :class="getCardClass(item)" shadow="hover">
                        <!-- 状态标签 -->
                        <div class="card-status" :class="getStatusClass(item)">
                            <el-tag :type="getStatusTagType(item)" size="small">
                                {{ item.chongdianzhuangZhuangtaiValue }}
                            </el-tag>
                        </div>

                        <!-- 充电桩图片 -->
                        <div class="card-image">
                            <img v-if="item.chongdianzhuangPhoto" :src="$base.url+item.chongdianzhuangPhoto" alt="充电桩图片">
                            <div v-else class="no-image">
                                <i class="el-icon-picture-outline"></i>
                                <span>暂无图片</span>
                            </div>
                        </div>

                        <!-- 充电桩信息 -->
                        <div class="card-content">
                            <h3 class="card-title">{{ item.chongdianzhuangName }}</h3>

                            <div class="card-info">
                                <div class="info-row">
                                    <i class="el-icon-location"></i>
                                    <span>{{ item.address || '地址未知' }}</span>
                                </div>
                                <div class="info-row" v-if="item.distance !== null">
                                    <i class="el-icon-place"></i>
                                    <span>距离 {{ item.distance.toFixed(2) }} km</span>
                                </div>
                                <div class="info-row">
                                    <i class="el-icon-price-tag"></i>
                                    <span class="price">¥{{ item.chongdianzhuangNewMoney }}/小时</span>
                                </div>
                                <div class="info-row">
                                    <i class="el-icon-time"></i>
                                    <span>可充时长: {{ item.chongdianzhuangKucunNumber }} 小时</span>
                                </div>
                                <div class="info-row">
                                    <span class="type-tag">{{ item.chongdianzhuangValue }}</span>
                                </div>
                            </div>

                            <!-- 操作按钮 -->
                            <div class="card-actions">
                                <el-button
                                    type="primary"
                                    size="small"
                                    icon="el-icon-view"
                                    @click="viewDetail(item)">
                                    查看详情
                                </el-button>
                                <el-button
                                    v-if="item.chongdianzhuangKucunNumber > 0 && item.chongdianzhuangZhuangtaiTypes === 1"
                                    type="success"
                                    size="small"
                                    icon="el-icon-shopping-cart"
                                    @click="handleReservation(item)">
                                    立即预约
                                </el-button>
                                <el-button
                                    v-else
                                    type="info"
                                    size="small"
                                    disabled
                                    icon="el-icon-shopping-cart">
                                    暂不可预约
                                </el-button>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>

            <!-- 分页 -->
            <el-row :gutter="20" v-if="dataList.length > 0">
                <el-col :span="24">
                    <el-pagination
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        :current-page="pageIndex"
                        :page-sizes="[12, 24, 36, 48]"
                        :page-size="pageSize"
                        :total="totalPage"
                        layout="total, sizes, prev, pager, next, jumper"
                        class="pagination-content">
                    </el-pagination>
                </el-col>
            </el-row>

            <!-- 无数据提示 -->
            <el-empty v-if="dataList.length === 0 && !dataListLoading" description="暂无符合条件的充电桩"></el-empty>
        </div>

        <!-- 详情弹窗 -->
        <el-dialog
            :title="detailTitle"
            :visible.sync="detailVisible"
            width="70%"
            top="5vh">
            <div v-if="currentItem" class="detail-content">
                <el-descriptions :column="2" border>
                    <el-descriptions-item label="充电桩名称">{{ currentItem.chongdianzhuangName }}</el-descriptions-item>
                    <el-descriptions-item label="充电桩编号">{{ currentItem.chongdianzhuangUuidNumber }}</el-descriptions-item>
                    <el-descriptions-item label="充电桩类型">{{ currentItem.chongdianzhuangValue }}</el-descriptions-item>
                    <el-descriptions-item label="实时状态">
                        <el-tag :type="getStatusTagType(currentItem)">{{ currentItem.chongdianzhuangZhuangtaiValue }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="价格">{{ currentItem.chongdianzhuangNewMoney }}元/小时</el-descriptions-item>
                    <el-descriptions-item label="可充时长">{{ currentItem.chongdianzhuangKucunNumber }}小时</el-descriptions-item>
                    <el-descriptions-item label="地址" :span="2">{{ currentItem.address }}</el-descriptions-item>
                    <el-descriptions-item label="经度">{{ currentItem.longitude }}</el-descriptions-item>
                    <el-descriptions-item label="纬度">{{ currentItem.latitude }}</el-descriptions-item>
                    <el-descriptions-item label="简介" :span="2">{{ currentItem.chongdianzhuangContent }}</el-descriptions-item>
                </el-descriptions>

                <div class="detail-actions">
                    <el-button type="primary" @click="handleReservation(currentItem)" size="large">
                        立即预约
                    </el-button>
                    <el-button @click="detailVisible = false" size="large">关闭</el-button>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        data() {
            return {
                searchForm: {
                    chongdianzhuangName: '',
                    chongdianzhuangTypes: '',
                    chongdianzhuangZhuangtaiTypes: '',
                    chongdianzhuangNewMoneyStart: null,
                    chongdianzhuangNewMoneyEnd: null,
                    chongdianzhuangKucunNumberStart: null,
                    chongdianzhuangKucunNumberEnd: null,
                    searchRadius: null,
                    sort: 'id',
                    order: 'desc',
                    onlyAvailable: false,
                    page: 1,
                    limit: 12
                },
                dataList: [],
                pageIndex: 1,
                pageSize: 12,
                totalPage: 0,
                dataListLoading: false,
                showAdvanced: false,
                locationLoading: false,
                currentLocation: null,
                chongdianzhuangTypesSelectSearch: [],
                chongdianzhuangZhuangtaiTypesList: [],
                statistics: {
                    total: 0,
                    available: 0,
                    idle: 0,
                    avgPrice: 0
                },
                detailVisible: false,
                detailTitle: '充电桩详情',
                currentItem: null,
                quickFilterTags: [
                    { label: '空闲桩', type: 'success', filter: { chongdianzhuangZhuangtaiTypes: 1 } },
                    { label: '使用中', type: 'warning', filter: { chongdianzhuangZhuangtaiTypes: 2 } },
                    { label: '价格<1元', type: 'info', filter: { chongdianzhuangNewMoneyEnd: 1 } },
                    { label: '可充电时长>10', type: 'danger', filter: { chongdianzhuangKucunNumberStart: 10 } }
                ]
            };
        },
        mounted() {
            this.init();
            this.getLocation();
        },
        methods: {
            init() {
                this.getChongdianzhuangTypesSelectSearch();
                this.getChongdianzhuangZhuangtaiTypesList();
                this.search();
            },
            // 获取充电桩类型下拉框
            getChongdianzhuangTypesSelectSearch() {
                this.$http({
                    url: 'dictionary/page',
                    method: "get",
                    params: {
                        page: 1,
                        limit: 100,
                        dicCode: 'chongdianzhuang_types'
                    }
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.chongdianzhuangTypesSelectSearch = data.data.list;
                    }
                });
            },
            // 获取充电桩状态下拉框
            getChongdianzhuangZhuangtaiTypesList() {
                this.$http({
                    url: 'dictionary/page',
                    method: "get",
                    params: {
                        page: 1,
                        limit: 100,
                        dicCode: 'chongdianzhuang_zhuangtai_types'
                    }
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.chongdianzhuangZhuangtaiTypesList = data.data.list;
                    }
                });
            },
            // 搜索
            search() {
                this.pageIndex = 1;
                this.getDataList();
            },
            // 获取数据列表
            getDataList() {
                this.dataListLoading = true;
                let params = { ...this.searchForm };

                // 移除空值
                Object.keys(params).forEach(key => {
                    if (params[key] === '' || params[key] === null || params[key] === undefined) {
                        delete params[key];
                    }
                });

                // 如果有位置信息，添加到查询参数
                if (this.currentLocation) {
                    params.userLongitude = this.currentLocation.longitude;
                    params.userLatitude = this.currentLocation.latitude;
                }

                this.$http({
                    url: 'chongdianzhuang/listEnhanced',
                    method: "get",
                    params: params
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.dataList = data.data.list;
                        this.totalPage = data.data.total;
                        this.calculateStatistics();
                    } else {
                        this.$message.error(data.msg);
                    }
                    this.dataListLoading = false;
                });
            },
            // 计算统计数据
            calculateStatistics() {
                const list = this.dataList;
                this.statistics.total = this.totalPage;
                this.statistics.available = list.filter(item => item.chongdianzhuangKucunNumber > 0).length;
                this.statistics.idle = list.filter(item => item.chongdianzhuangZhuangtaiTypes === 1).length;

                const prices = list.map(item => item.chongdianzhuangNewMoney).filter(p => p > 0);
                this.statistics.avgPrice = prices.length > 0
                    ? (prices.reduce((a, b) => a + b, 0) / prices.length).toFixed(2)
                    : '0.00';
            },
            // 重置搜索表单
            resetSearchForm() {
                this.searchForm = {
                    chongdianzhuangName: '',
                    chongdianzhuangTypes: '',
                    chongdianzhuangZhuangtaiTypes: '',
                    chongdianzhuangNewMoneyStart: null,
                    chongdianzhuangNewMoneyEnd: null,
                    chongdianzhuangKucunNumberStart: null,
                    chongdianzhuangKucunNumberEnd: null,
                    searchRadius: null,
                    sort: 'id',
                    order: 'desc',
                    onlyAvailable: false,
                    page: 1,
                    limit: 12
                };
                this.search();
            },
            // 快速筛选
            handleQuickFilter(tag) {
                Object.assign(this.searchForm, tag.filter);
                this.search();
            },
            // 获取当前位置
            getCurrentLocation() {
                if (!navigator.geolocation) {
                    this.$message.error('浏览器不支持地理定位');
                    return;
                }

                this.locationLoading = true;
                navigator.geolocation.getCurrentPosition(
                    (position) => {
                        this.currentLocation = {
                            latitude: position.coords.latitude,
                            longitude: position.coords.longitude
                        };
                        this.$message.success('位置获取成功');
                        this.locationLoading = false;
                        // 设置按距离排序
                        this.searchForm.sort = 'distance';
                        this.search();
                    },
                    (error) => {
                        this.$message.error('获取位置失败: ' + error.message);
                        this.locationLoading = false;
                    }
                );
            },
            // 自动获取位置
            getLocation() {
                if (navigator.geolocation) {
                    navigator.geolocation.getCurrentPosition(
                        (position) => {
                            this.currentLocation = {
                                latitude: position.coords.latitude,
                                longitude: position.coords.longitude
                            };
                        },
                        () => {
                            console.log('自动获取位置失败');
                        }
                    );
                }
            },
            // 获取卡片样式
            getCardClass(item) {
                return {
                    'card-idle': item.chongdianzhuangZhuangtaiTypes === 1,
                    'card-in-use': item.chongdianzhuangZhuangtaiTypes === 2,
                    'card-unavailable': item.chongdianzhuangKucunNumber === 0
                };
            },
            // 获取状态样式
            getStatusClass(item) {
                return {
                    'status-idle': item.chongdianzhuangZhuangtaiTypes === 1,
                    'status-in-use': item.chongdianzhuangZhuangtaiTypes === 2
                };
            },
            // 获取状态标签类型
            getStatusTagType(item) {
                const types = {
                    1: 'success',
                    2: 'warning'
                };
                return types[item.chongdianzhuangZhuangtaiTypes] || 'info';
            },
            // 查看详情
            viewDetail(item) {
                this.currentItem = item;
                this.detailVisible = true;
            },
            // 处理预约
            handleReservation(item) {
                this.$message.info('预约功能开发中');
                // 这里可以跳转到预约页面或打开预约对话框
            },
            // 每页数
            sizeChangeHandle(val) {
                this.pageSize = val;
                this.pageIndex = 1;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle(val) {
                this.pageIndex = val;
                this.getDataList();
            }
        }
    };
</script>

<style scoped>
    .query-section {
        margin-bottom: 20px;
    }

    .filter-card {
        margin-bottom: 0;
    }

    .card-header {
        font-size: 18px;
        font-weight: bold;
        color: #409EFF;
    }

    .filter-form {
        margin-top: 15px;
    }

    .separator {
        margin: 0 10px;
        color: #999;
    }

    .quick-filters {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid #eee;
    }

    .filter-tag {
        margin-left: 10px;
        cursor: pointer;
    }

    .location-section {
        margin-top: 15px;
        padding-top: 15px;
        border-top: 1px solid #eee;
    }

    .location-info {
        margin-left: 15px;
        color: #67C23A;
        font-size: 14px;
    }

    .stats-card {
        margin-bottom: 20px;
    }

    .stat-item {
        text-align: center;
        padding: 10px;
    }

    .stat-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 5px;
    }

    .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #409EFF;
    }

    .stat-value.available {
        color: #67C23A;
    }

    .stat-value.idle {
        color: #E6A23C;
    }

    .stat-value.price {
        color: #F56C6C;
    }

    .charging-list {
        margin: 20px 0;
    }

    .charging-card {
        margin-bottom: 20px;
        position: relative;
        transition: all 0.3s;
    }

    .charging-card:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
    }

    .card-status {
        position: absolute;
        top: 10px;
        right: 10px;
        z-index: 1;
    }

    .card-image {
        width: 100%;
        height: 160px;
        overflow: hidden;
        background: #f5f7fa;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 15px;
    }

    .card-image img {
        width: 100%;
        height: 100%;
        object-fit: cover;
    }

    .no-image {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #c0c4cc;
        font-size: 14px;
    }

    .no-image i {
        font-size: 48px;
        margin-bottom: 10px;
    }

    .card-content {
        padding: 0 5px;
    }

    .card-title {
        font-size: 16px;
        font-weight: bold;
        margin-bottom: 10px;
        color: #303133;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
    }

    .card-info {
        margin-bottom: 15px;
    }

    .info-row {
        font-size: 13px;
        color: #606266;
        margin-bottom: 8px;
        display: flex;
        align-items: center;
    }

    .info-row i {
        margin-right: 5px;
        color: #409EFF;
    }

    .info-row .price {
        color: #F56C6C;
        font-weight: bold;
        font-size: 14px;
    }

    .type-tag {
        background: #409EFF;
        color: white;
        padding: 2px 8px;
        border-radius: 4px;
        font-size: 12px;
    }

    .card-actions {
        display: flex;
        gap: 10px;
    }

    .card-actions .el-button {
        flex: 1;
    }

    .pagination-content {
        text-align: center;
        margin-top: 20px;
    }

    .detail-content {
        padding: 20px;
    }

    .detail-actions {
        text-align: center;
        margin-top: 30px;
    }

    .detail-actions .el-button {
        width: 200px;
    }

    /* 卡片状态样式 */
    .card-idle {
        border-left: 4px solid #67C23A;
    }

    .card-in-use {
        border-left: 4px solid #E6A23C;
    }

    .card-unavailable {
        border-left: 4px solid #909399;
        opacity: 0.7;
    }
</style>
