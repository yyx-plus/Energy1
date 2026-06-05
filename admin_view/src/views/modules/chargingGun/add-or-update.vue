<template>
    <div class="addEdit-block">
        <el-form class="detail-form-content" ref="ruleForm" :model="ruleForm" :rules="rules" label-width="auto">
            <el-row>
                <input id="updateId" name="id" type="hidden">
                <el-col :span="12">
                    <el-form-item class="select" v-if="type!='info'" label="所属充电桩" prop="stationId">
                        <el-select v-model="ruleForm.stationId" :disabled="ro.stationId" placeholder="请选择充电桩">
                            <el-option
                                v-for="item in stationList"
                                :key="item.id"
                                :label="item.chongdianzhuangName"
                                :value="item.id">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="所属充电桩" prop="stationName">
                            <el-input v-model="ruleForm.stationName" placeholder="所属充电桩" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type!='info'" label="枪头编号" prop="gunNo">
                        <el-input v-model="ruleForm.gunNo" placeholder="枪头编号" clearable :readonly="ro.gunNo"></el-input>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="枪头编号" prop="gunNo">
                            <el-input v-model="ruleForm.gunNo" placeholder="枪头编号" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type!='info'" label="枪头名称" prop="gunName">
                        <el-input v-model="ruleForm.gunName" placeholder="枪头名称" clearable :readonly="ro.gunName"></el-input>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="枪头名称" prop="gunName">
                            <el-input v-model="ruleForm.gunName" placeholder="枪头名称" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="select" v-if="type!='info'" label="接口类型" prop="gunType">
                        <el-select v-model="ruleForm.gunType" :disabled="ro.gunType" placeholder="请选择接口类型">
                            <el-option label="国标交流" :value="1"></el-option>
                            <el-option label="国标直流" :value="2"></el-option>
                            <el-option label="欧标" :value="3"></el-option>
                            <el-option label="特斯拉" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="接口类型" prop="gunTypeName">
                            <el-input v-model="ruleForm.gunTypeName" placeholder="接口类型" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="input" v-if="type!='info'" label="功率(kW)" prop="powerKw">
                        <el-input v-model="ruleForm.powerKw" placeholder="功率(kW)" clearable :readonly="ro.powerKw"></el-input>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="功率(kW)" prop="powerKw">
                            <el-input v-model="ruleForm.powerKw" placeholder="功率(kW)" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="select" v-if="type!='info'" label="状态" prop="status">
                        <el-select v-model="ruleForm.status" :disabled="ro.status" placeholder="请选择状态">
                            <el-option label="空闲" :value="1"></el-option>
                            <el-option label="占用" :value="2"></el-option>
                            <el-option label="故障" :value="3"></el-option>
                            <el-option label="离线" :value="4"></el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="状态" prop="statusName">
                            <el-input v-model="ruleForm.statusName" placeholder="状态" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
            </el-row>
            <el-form-item class="btn">
                <el-button v-if="type!='info'" type="primary" class="btn-success" @click="onSubmit">提交</el-button>
                <el-button v-if="type!='info'" class="btn-close" @click="back()">取消</el-button>
                <el-button v-if="type=='info'" class="btn-close" @click="back()">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
    import styleJs from "../../../utils/style.js";
    import { isNumber } from "@/utils/validate";

    export default {
        data() {
            return {
                addEditForm: null,
                id: '',
                type: '',
                sessionTable: "",
                role: "",
                userId: "",
                ro: {
                    stationId: false,
                    gunNo: false,
                    gunName: false,
                    gunType: false,
                    powerKw: false,
                    status: false,
                },
                ruleForm: {
                    stationId: '',
                    gunNo: '',
                    gunName: '',
                    gunType: 1,
                    powerKw: '',
                    status: 1,
                },
                stationList: [],
                rules: {
                    stationId: [{ required: true, message: '所属充电桩不能为空', trigger: 'change' }],
                    gunNo: [{ required: true, message: '枪头编号不能为空', trigger: 'blur' }],
                    gunName: [{ required: true, message: '枪头名称不能为空', trigger: 'blur' }],
                    gunType: [{ required: true, message: '接口类型不能为空', trigger: 'change' }],
                    powerKw: [
                        { required: true, message: '功率不能为空', trigger: 'blur' },
                        { validator: this.validateNumber, trigger: 'blur' }
                    ],
                    status: [{ required: true, message: '状态不能为空', trigger: 'change' }],
                }
            };
        },
        mounted() {
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");
            this.getStationList();
        },
        methods: {
            validateNumber(rule, value, callback) {
                if(!value || isNaN(value)) {
                    callback(new Error('请输入有效的数字'));
                } else {
                    callback();
                }
            },
            getStationList() {
                this.$http({
                    url: "chongdianzhuang/page?page=1&limit=1000&sort=id",
                    method: "get"
                }).then(({data}) => {
                    if(data && data.code === 0){
                        this.stationList = data.data.list;
                    }
                });
            },
            init(id, type) {
                this.type = type;
                if(id) {
                    this.id = id;
                    this.$http({
                        url: `chargingGun/info/${id}`,
                        method: "get"
                    }).then(({data}) => {
                        if(data && data.code === 0){
                            this.ruleForm = data.data;
                            const station = this.stationList.find(s => s.id == this.ruleForm.stationId);
                            this.ruleForm.stationName = station ? station.chongdianzhuangName : '-';
                            const gunTypeMap = {1: '国标交流', 2: '国标直流', 3: '欧标', 4: '特斯拉'};
                            this.ruleForm.gunTypeName = gunTypeMap[this.ruleForm.gunType] || '-';
                            const statusMap = {1: '空闲', 2: '占用', 3: '故障', 4: '离线'};
                            this.ruleForm.statusName = statusMap[this.ruleForm.status] || '-';
                        }
                    });
                }
            },
            onSubmit() {
                this.$refs["ruleForm"].validate((valid) => {
                    if(valid) {
                        let url = this.id ? "chargingGun/update" : "chargingGun/save";
                        let data = {
                            stationId: this.ruleForm.stationId,
                            gunNo: this.ruleForm.gunNo,
                            gunName: this.ruleForm.gunName,
                            gunType: this.ruleForm.gunType,
                            powerKw: this.ruleForm.powerKw,
                            status: this.ruleForm.status,
                        };
                        if(this.id) data.id = this.id;
                        this.$http({
                            url: url,
                            method: "post",
                            data: data
                        }).then(({data}) => {
                            if(data && data.code === 0){
                                this.$message({
                                    message: "操作成功",
                                    type: "success",
                                    duration: 1500,
                                    onClose: () => {
                                        this.back();
                                    }
                                });
                            } else {
                                this.$message.error(data.msg);
                            }
                        });
                    }
                });
            },
            back() {
                this.parent.showFlag = true;
                this.parent.addOrUpdateFlag = false;
                this.parent.getDataList();
            }
        }
    }
</script>

<style lang="scss" scoped>
    .addEdit-block {
        margin: -10px;
    }
    .detail-form-content {
        padding: 12px;
    }
    .btn .el-button {
        padding: 10px 20px;
        margin-right: 10px;
    }
</style>
