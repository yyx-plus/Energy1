<template>
    <div class="main-content">
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content">
                <el-row :gutter="20" class="slt" :style="{justifyContent:contents.searchBoxPosition=='1'?'flex-start':contents.searchBoxPosition=='2'?'center':'flex-end'}">
                    <el-form-item :label="contents.inputTitle == 1 ? '充电桩' : ''">
                        <el-select v-model="searchForm.stationId" placeholder="请选择充电桩" clearable>
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option
                                v-for="item in stationList"
                                :key="item.id"
                                :label="item.chongdianzhuangName"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item :label="contents.inputTitle == 1 ? '枪头名称' : ''">
                        <el-input prefix-icon="el-icon-search" v-model="searchForm.gunName" placeholder="枪头名称" clearable></el-input>
                    </el-form-item>
                    <el-form-item :label="contents.inputTitle == 1 ? '状态' : ''">
                        <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option label="空闲" :value="1"></el-option>
                            <el-option label="占用" :value="2"></el-option>
                            <el-option label="故障" :value="3"></el-option>
                            <el-option label="离线" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="success" @click="search()">查询<i class="el-icon-search el-icon--right"/></el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad" :style="{justifyContent:contents.btnAdAllBoxPosition=='1'?'flex-start':contents.btnAdAllBoxPosition=='2'?'center':'flex-end'}">
                    <el-form-item>
                        <el-button v-if="isAuth('chargingGun','新增')" type="success" icon="el-icon-plus" @click="addOrUpdateHandler()">新增</el-button>
                        &nbsp;
                        <el-button v-if="isAuth('chargingGun','删除')" :disabled="dataListSelections.length <= 0" type="danger" icon="el-icon-delete" @click="deleteHandler()">删除</el-button>
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
                          :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle"
                          :border="contents.tableBorder"
                          :fit="contents.tableFit"
                          :stripe="contents.tableStripe"
                          :row-style="rowStyle"
                          :cell-style="cellStyle"
                          :style="{width: '100%',fontSize:contents.tableContentFontSize,color:contents.tableContentFontColor}"
                          v-if="isAuth('chargingGun','查看')"
                          :data="dataList"
                          v-loading="dataListLoading"
                          @selection-change="selectionChangeHandler">
                    <el-table-column v-if="contents.tableSelection" type="selection" header-align="center" align="center" width="50"></el-table-column>
                    <el-table-column label="索引" v-if="contents.tableIndex" type="index" width="50" />
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="gunNo" header-align="center" label="枪头编号">
                        <template slot-scope="scope">{{scope.row.gunNo}}</template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="gunName" header-align="center" label="枪头名称">
                        <template slot-scope="scope">{{scope.row.gunName}}</template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="stationName" header-align="center" label="所属充电桩">
                        <template slot-scope="scope">{{scope.row.stationName || '-'}}</template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="gunType" header-align="center" label="接口类型">
                        <template slot-scope="scope">{{gunTypeFormat(scope.row.gunType)}}</template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="powerKw" header-align="center" label="功率(kW)">
                        <template slot-scope="scope">{{scope.row.powerKw}}</template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="status" header-align="center" label="状态">
                        <template slot-scope="scope">
                            <el-tag :type="statusTagType(scope.row.status)">{{statusFormat(scope.row.status)}}</el-tag>
                        </template>
                    </el-table-column>
                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="createTime" header-align="center" label="创建时间">
                        <template slot-scope="scope">{{scope.row.createTime}}</template>
                    </el-table-column>
                    <el-table-column width="300" :align="contents.tableAlign" header-align="center" label="操作">
                        <template slot-scope="scope">
                            <el-button v-if="isAuth('chargingGun','查看')" type="success" icon="el-icon-tickets" size="mini" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
                            <el-button v-if="isAuth('chargingGun','修改')" type="primary" icon="el-icon-edit" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
                            <el-button v-if="isAuth('chargingGun','删除')" type="danger" icon="el-icon-delete" size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>
    </div>
</template>

<script>
    import AddOrUpdate from "./add-or-update";
    import styleJs from "../../../utils/style.js";

    export default {
        data() {
            return {
                searchForm: {
                    stationId: "",
                    gunName: "",
                    status: ""
                },
                sessionTable: "",
                role: "",
                userId: "",
                dataList: [],
                stationList: [],
                dataListLoading: false,
                dataListSelections: [],
                showFlag: true,
                addOrUpdateFlag: false,
                contents: null,
                layouts: '',
            };
        },
        created() {
            this.contents = styleJs.listStyle();
            this.init();
            this.getStationList().then(() => {
                if(this.$route.query.stationId) {
                    this.searchForm.stationId = Number(this.$route.query.stationId);
                }
                this.getDataList();
            });
            this.contentStyleChange();
        },
        mounted() {
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");
        },
        components: {
            AddOrUpdate,
        },
        methods: {
            gunTypeFormat(type) {
                const map = {1: '国标交流', 2: '国标直流', 3: '欧标', 4: '特斯拉'};
                return map[type] || '-';
            },
            statusFormat(status) {
                const map = {1: '空闲', 2: '占用', 3: '故障', 4: '离线'};
                return map[status] || '-';
            },
            statusTagType(status) {
                const map = {1: 'success', 2: 'warning', 3: 'danger', 4: 'info'};
                return map[status] || '';
            },
            contentStyleChange() {
                this.contentSearchStyleChange();
                this.contentBtnAdAllStyleChange();
                this.contentSearchBtnStyleChange();
                this.contentTableBtnStyleChange();
                this.contentPageStyleChange();
            },
            contentSearchStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el => {
                        let textAlign = 'left';
                        if(this.contents.inputFontPosition == 2) textAlign = 'center';
                        if(this.contents.inputFontPosition == 3) textAlign = 'right';
                        el.style.textAlign = textAlign;
                        el.style.height = this.contents.inputHeight;
                        el.style.lineHeight = this.contents.inputHeight;
                        el.style.color = this.contents.inputFontColor;
                        el.style.fontSize = this.contents.inputFontSize;
                        el.style.borderWidth = this.contents.inputBorderWidth;
                        el.style.borderStyle = this.contents.inputBorderStyle;
                        el.style.borderColor = this.contents.inputBorderColor;
                        el.style.borderRadius = this.contents.inputBorderRadius;
                        el.style.backgroundColor = this.contents.inputBgColor;
                    });
                    if(this.contents.inputTitle) {
                        document.querySelectorAll('.form-content .slt .el-form-item__label').forEach(el => {
                            el.style.color = this.contents.inputTitleColor;
                            el.style.fontSize = this.contents.inputTitleSize;
                            el.style.lineHeight = this.contents.inputHeight;
                        });
                    }
                });
            },
            contentBtnAdAllStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .ad .el-button--success').forEach(el => {
                        el.style.height = this.contents.btnAdAllHeight;
                        el.style.color = this.contents.btnAdAllAddFontColor;
                        el.style.fontSize = this.contents.btnAdAllFontSize;
                        el.style.borderWidth = this.contents.btnAdAllBorderWidth;
                        el.style.borderStyle = this.contents.btnAdAllBorderStyle;
                        el.style.borderColor = this.contents.btnAdAllBorderColor;
                        el.style.borderRadius = this.contents.btnAdAllBorderRadius;
                        el.style.backgroundColor = this.contents.btnAdAllAddBgColor;
                    });
                });
            },
            contentSearchBtnStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .slt .el-button--success').forEach(el => {
                        el.style.height = this.contents.searchBtnHeight;
                        el.style.color = this.contents.searchBtnFontColor;
                        el.style.fontSize = this.contents.searchBtnFontSize;
                        el.style.borderWidth = this.contents.searchBtnBorderWidth;
                        el.style.borderStyle = this.contents.searchBtnBorderStyle;
                        el.style.borderColor = this.contents.searchBtnBorderColor;
                        el.style.borderRadius = this.contents.searchBtnBorderRadius;
                        el.style.backgroundColor = this.contents.searchBtnBgColor;
                    });
                });
            },
            contentTableBtnStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.tables .el-button--default').forEach(el => {
                        el.style.height = this.contents.tableBtnHeight;
                        el.style.color = this.contents.tableBtnDetailFontColor;
                        el.style.fontSize = this.contents.tableBtnFontSize;
                        el.style.borderWidth = this.contents.tableBtnBorderWidth;
                        el.style.borderStyle = this.contents.tableBtnBorderStyle;
                        el.style.borderColor = this.contents.tableBtnBorderColor;
                        el.style.borderRadius = this.contents.tableBtnBorderRadius;
                        el.style.backgroundColor = this.contents.tableBtnDetailBgColor;
                    });
                });
            },
            contentPageStyleChange() {
                let arr = [];
                if(this.contents.pageTotal) arr.push('total');
                if(this.contents.pageSizes) arr.push('sizes');
                if(this.contents.pagePrevNext) {
                    arr.push('prev');
                    if(this.contents.pagePager) arr.push('pager');
                    arr.push('next');
                }
                if(this.contents.pageJumper) arr.push('jumper');
                this.layouts = arr.join();
            },
            init() {},
            search() {
                this.getDataList();
            },
            getStationList() {
                return new Promise((resolve) => {
                    this.$http({
                        url: "chongdianzhuang/page?page=1&limit=1000&sort=id",
                        method: "get"
                    }).then(({data}) => {
                        if(data && data.code === 0){
                            this.stationList = data.data.list;
                        }
                        resolve();
                    });
                });
            },
            getDataList() {
                this.dataListLoading = true;
                let params = {};
                if(this.searchForm.stationId !== '' && this.searchForm.stationId !== undefined) {
                    params['stationId'] = this.searchForm.stationId;
                }
                this.$http({
                    url: "chargingGun/list",
                    method: "get",
                    params: params
                }).then(({data}) => {
                    if(data && data.code === 0){
                        let list = data.data;
                        // 客户端过滤
                        if(this.searchForm.gunName) {
                            list = list.filter(item => item.gunName && item.gunName.includes(this.searchForm.gunName));
                        }
                        if(this.searchForm.status !== '' && this.searchForm.status !== undefined) {
                            list = list.filter(item => item.status == this.searchForm.status);
                        }
                        // 填充充电桩名称
                        list.forEach(item => {
                            const station = this.stationList.find(s => s.id == item.stationId);
                            item.stationName = station ? station.chongdianzhuangName : '-';
                        });
                        this.dataList = list;
                    } else {
                        this.dataList = [];
                    }
                    this.dataListLoading = false;
                });
            },
            selectionChangeHandler(val) {
                this.dataListSelections = val;
            },
            addOrUpdateHandler(id, type) {
                this.showFlag = false;
                this.addOrUpdateFlag = true;
                if(type != 'info') type = 'else';
                this.$nextTick(() => {
                    this.$refs.addOrUpdate.init(id, type);
                });
            },
            deleteHandler(id) {
                var ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id));
                this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$http({
                        url: "chargingGun/delete",
                        method: "post",
                        data: ids
                    }).then(({data}) => {
                        if(data && data.code === 0){
                            this.$message({message: "操作成功", type: "success", duration: 1500, onClose: () => { this.search(); }});
                        } else {
                            this.$message.error(data.msg);
                        }
                    });
                });
            },
            headerRowStyle({row, rowIndex}) {
                return {color: this.contents.tableHeaderFontColor, fontSize: this.contents.tableHeaderFontSize};
            },
            headerCellStyle({row, rowIndex}) {
                return {backgroundColor: this.contents.tableHeaderBgColor};
            },
            rowStyle({row, rowIndex}) {
                return {fontSize: this.contents.tableContentFontSize, color: this.contents.tableContentFontColor};
            },
            cellStyle({row, rowIndex}) {
                return {};
            }
        }
    }
</script>

<style lang="scss" scoped>
    .slt { margin: 0 !important; display: flex; }
    .ad { margin: 0 !important; display: flex; }
    .el-button+.el-button { margin: 0; }
    .tables {
        ::v-deep .el-button--success {
            height: 35px; color: rgba(4, 4, 4, 1); font-size: 14px;
            border-width: 0px; border-style: solid; border-color: rgba(1, 1, 1, 1);
            border-radius: 4px; background-color: rgba(253, 228, 79, 1);
        }
        ::v-deep .el-button--primary {
            height: 35px; color: rgba(8, 8, 8, 1); font-size: 14px;
            border-width: 0px; border-style: solid; border-color: rgba(1, 1, 1, 1);
            border-radius: 4px; background-color: rgba(240, 125, 48, 1);
        }
        ::v-deep .el-button--danger {
            height: 35px; color: rgba(8, 8, 8, 1); font-size: 14px;
            border-width: 0px; border-style: solid; border-color: rgba(1, 1, 1, 1);
            border-radius: 4px; background-color: rgba(233, 95, 64, 1);
        }
        ::v-deep .el-button { margin: 4px; }
    }
</style>
