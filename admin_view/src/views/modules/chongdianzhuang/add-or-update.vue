<template>
    <div class="addEdit-block">
        <el-form
                class="detail-form-content"
                ref="ruleForm"
                :model="ruleForm"
                :rules="rules"
                label-width="auto"
                >
            <el-row>
                <input id="updateId" name="id" type="hidden">
               <el-col :span="12">
                   <el-form-item class="input" v-if="type!='info'"  label="充电桩名称" prop="chongdianzhuangName">
                       <el-input v-model="ruleForm.chongdianzhuangName"
                                 placeholder="充电桩名称" clearable  :readonly="ro.chongdianzhuangName"></el-input>
                   </el-form-item>
                   <div v-else-if="type=='info'">
                       <el-form-item class="input" label="充电桩名称" prop="chongdianzhuangName">
                           <el-input v-model="ruleForm.chongdianzhuangName"
                                     placeholder="充电桩名称" readonly></el-input>
                       </el-form-item>
                   </div>
               </el-col>
               <el-col :span="12">
                   <el-form-item class="input" v-if="type!='info'"  label="充电桩编号" prop="chongdianzhuangUuidNumber">
                       <el-input v-model="ruleForm.chongdianzhuangUuidNumber"
                                 placeholder="充电桩编号" clearable  :readonly="ro.chongdianzhuangUuidNumber"></el-input>
                   </el-form-item>
                   <div v-else-if="type=='info'">
                       <el-form-item class="input" label="充电桩编号" prop="chongdianzhuangUuidNumber">
                           <el-input v-model="ruleForm.chongdianzhuangUuidNumber"
                                     placeholder="充电桩编号" readonly></el-input>
                       </el-form-item>
                   </div>
               </el-col>
                <el-col :span="12">
                    <el-form-item class="upload" v-if="type!='info' && !ro.chongdianzhuangPhoto" label="充电桩照片" prop="chongdianzhuangPhoto">
                        <file-upload
                            tip="点击上传充电桩照片"
                            action="file/upload"
                            :limit="3"
                            :multiple="true"
                            :fileUrls="ruleForm.chongdianzhuangPhoto?$base.url+ruleForm.chongdianzhuangPhoto:''"
                            @change="chongdianzhuangPhotoUploadChange"
                        ></file-upload>
                    </el-form-item>
                    <div v-else>
                        <el-form-item v-if="ruleForm.chongdianzhuangPhoto" label="充电桩照片" prop="chongdianzhuangPhoto">
                            <img style="margin-right:20px;" v-bind:key="index" v-for="(item,index) in (ruleForm.chongdianzhuangPhoto || '').split(',')" :src="$base.url+item" width="100" height="100">
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="select" v-if="type!='info'"  label="充电桩类型" prop="chongdianzhuangTypes">
                        <el-select v-model="ruleForm.chongdianzhuangTypes" :disabled="ro.chongdianzhuangTypes" placeholder="请选择充电桩类型">
                            <el-option
                                v-for="(item,index) in chongdianzhuangTypesOptions"
                                v-bind:key="item.codeIndex"
                                :label="item.indexName"
                                :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="充电桩类型" prop="chongdianzhuangValue">
                        <el-input v-model="ruleForm.chongdianzhuangValue"
                            placeholder="充电桩类型" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
                <el-col :span="12">
                    <el-form-item class="select" v-if="type!='info'"  label="充电桩状态" prop="chongdianzhuangZhuangtaiTypes">
                        <el-select v-model="ruleForm.chongdianzhuangZhuangtaiTypes" :disabled="ro.chongdianzhuangZhuangtaiTypes" placeholder="请选择充电桩状态">
                            <el-option
                                v-for="(item,index) in chongdianzhuangZhuangtaiTypesOptions"
                                v-bind:key="item.codeIndex"
                                :label="item.indexName"
                                :value="item.codeIndex">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="充电桩状态" prop="chongdianzhuangZhuangtaiValue">
                        <el-input v-model="ruleForm.chongdianzhuangZhuangtaiValue"
                            placeholder="充电桩状态" readonly></el-input>
                        </el-form-item>
                    </div>
                </el-col>
               <el-col :span="12">
                   <el-form-item class="input" v-if="type!='info'"  label="可充时长" prop="chongdianzhuangKucunNumber">
                       <el-input v-model="ruleForm.chongdianzhuangKucunNumber"
                                 placeholder="可充时长" clearable  :readonly="ro.chongdianzhuangKucunNumber"></el-input>
                   </el-form-item>
                   <div v-else-if="type=='info'">
                       <el-form-item class="input" label="可充时长" prop="chongdianzhuangKucunNumber">
                           <el-input v-model="ruleForm.chongdianzhuangKucunNumber"
                                     placeholder="可充时长" readonly></el-input>
                       </el-form-item>
                   </div>
               </el-col>
               <el-col :span="12">
                   <el-form-item class="input" v-if="type!='info'"  label="金额/小时" prop="chongdianzhuangNewMoney">
                       <el-input v-model="ruleForm.chongdianzhuangNewMoney"
                                 placeholder="金额/小时" clearable  :readonly="ro.chongdianzhuangNewMoney"></el-input>
                   </el-form-item>
                   <div v-else-if="type=='info'">
                       <el-form-item class="input" label="金额/小时" prop="chongdianzhuangNewMoney">
                           <el-input v-model="ruleForm.chongdianzhuangNewMoney"
                                     placeholder="金额/小时" readonly></el-input>
                       </el-form-item>
                   </div>
               </el-col>
                <el-col :span="24">
                    <el-form-item v-if="type!='info'"  label="充电桩介绍" prop="chongdianzhuangContent">
                        <editor style="min-width: 200px; max-width: 600px;"
                                v-model="ruleForm.chongdianzhuangContent"
                                class="editor"
                                action="file/upload">
                        </editor>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item v-if="ruleForm.chongdianzhuangContent" label="充电桩介绍" prop="chongdianzhuangContent">
                            <span v-html="ruleForm.chongdianzhuangContent"></span>
                        </el-form-item>
                    </div>
                </el-col>

                <!-- 是否快充 -->
                <el-col :span="12">
                    <el-form-item class="switch" v-if="type!='info'" label="是否快充" prop="isFastCharge">
                        <el-switch
                            v-model="ruleForm.isFastCharge"
                            :active-value="1"
                            :inactive-value="0"
                            :disabled="ro.isFastCharge"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            active-text="是"
                            inactive-text="否">
                        </el-switch>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="是否快充" prop="isFastCharge">
                            <el-tag :type="ruleForm.isFastCharge === 1 ? 'success' : 'info'">
                                {{ ruleForm.isFastCharge === 1 ? '是' : '否' }}
                            </el-tag>
                        </el-form-item>
                    </div>
                </el-col>

                <!-- 是否免费停车 -->
                <el-col :span="12">
                    <el-form-item class="switch" v-if="type!='info'" label="是否免费停车" prop="isFreeParking">
                        <el-switch
                            v-model="ruleForm.isFreeParking"
                            :active-value="1"
                            :inactive-value="0"
                            :disabled="ro.isFreeParking"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            active-text="是"
                            inactive-text="否">
                        </el-switch>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="是否免费停车" prop="isFreeParking">
                            <el-tag :type="ruleForm.isFreeParking === 1 ? 'success' : 'info'">
                                {{ ruleForm.isFreeParking === 1 ? '是' : '否' }}
                            </el-tag>
                        </el-form-item>
                    </div>
                </el-col>

                <!-- 地图位置选择 -->
                <el-col :span="24">
                    <el-form-item class="input" v-if="type!='info'" label="充电桩位置" prop="address">
                        <div class="map-container" style="height: 400px; margin-bottom: 20px;">
                            <div id="mapContainer" style="width: 100%; height: 100%;"></div>
                        </div>
                        <div class="address-input" style="display: flex; margin-bottom: 10px;">
                            <el-input v-model="searchAddress" placeholder="输入地址搜索位置" style="margin-right: 10px;"></el-input>
                            <el-button type="primary" @click="searchLocation">搜索位置</el-button>
                        </div>
                        <div style="display: flex; justify-content: space-between;">
                            <div>
                                <el-input v-model="ruleForm.address" placeholder="地址" readonly style="margin-bottom: 10px;"></el-input>
                                <div style="display: flex;">
                                    <el-input v-model="ruleForm.longitude" placeholder="经度" readonly style="margin-right: 10px;"></el-input>
                                    <el-input v-model="ruleForm.latitude" placeholder="纬度" readonly></el-input>
                                </div>
                            </div>
                        </div>
                    </el-form-item>
                    <div v-else-if="type=='info'">
                        <el-form-item class="input" label="充电桩位置" prop="address">
                            <el-input v-model="ruleForm.address" placeholder="地址" readonly></el-input>
                            <div style="display: flex; margin-top: 10px;">
                                <el-input v-model="ruleForm.longitude" placeholder="经度" readonly style="margin-right: 10px;"></el-input>
                                <el-input v-model="ruleForm.latitude" placeholder="纬度" readonly></el-input>
                            </div>
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
    // 数字，邮件，手机，url，身份证校验
    import { isNumber,isIntNumer,isEmail,isPhone, isMobile,isURL,checkIdCard } from "@/utils/validate";
    import utilsJs, {getYearFormat,getMonthFormat,getDateFormat,getDatetimeFormat} from "../../../utils/utils.js";
    export default {
        data() {
            return {
                addEditForm:null,
                id: '',
                type: '',
                sessionTable : "",//登录账户所在表名
                role : "",//权限
                userId:"",//当前登录人的id
                ro:{
                    chongdianzhuangName: false,
                    chongdianzhuangUuidNumber: false,
                    chongdianzhuangPhoto: false,
                    chongdianzhuangTypes: false,
                    chongdianzhuangZhuangtaiTypes: false,
                    chongdianzhuangKucunNumber: false,
                    chongdianzhuangNewMoney: false,
                    chongdianzhuangContent: false,
                    shangxiaTypes: false,
                    chongdianzhuangDelete: false,
                    insertTime: false,
                },
                ruleForm: {
                    chongdianzhuangName: '',
                    chongdianzhuangUuidNumber: new Date().getTime(),
                    chongdianzhuangPhoto: '',
                    chongdianzhuangTypes: '',
                    chongdianzhuangZhuangtaiTypes: '',
                    chongdianzhuangKucunNumber: '',
                    chongdianzhuangNewMoney: '',
                    chongdianzhuangContent: '',
                    shangxiaTypes: '',
                    chongdianzhuangDelete: '',
                    insertTime: '',
                    address: '',
                    longitude: '',
                    latitude: '',
                    isFastCharge: 0,
                    isFreeParking: 0,
                },
                chongdianzhuangTypesOptions : [],
                chongdianzhuangZhuangtaiTypesOptions : [],
                shangxiaTypesOptions : [],
                rules: {
                   chongdianzhuangName: [
                              { required: true, message: '充电桩名称不能为空', trigger: 'blur' },
                          ],
                   chongdianzhuangUuidNumber: [
                              { required: true, message: '充电桩编号不能为空', trigger: 'blur' },
                          ],
                   chongdianzhuangPhoto: [
                              { required: true, message: '充电桩照片不能为空', trigger: 'blur' },
                          ],
                   chongdianzhuangTypes: [
                              { required: true, message: '充电桩类型不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]*$/,
                                  message: '只允许输入整数',
                                  trigger: 'blur'
                              }
                          ],
                   chongdianzhuangZhuangtaiTypes: [
                              { required: true, message: '充电桩状态不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]*$/,
                                  message: '只允许输入整数',
                                  trigger: 'blur'
                              }
                          ],
                   chongdianzhuangKucunNumber: [
                              { required: true, message: '可充时长不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]*$/,
                                  message: '只允许输入整数',
                                  trigger: 'blur'
                              }
                          ],
                   chongdianzhuangNewMoney: [
                              { required: true, message: '金额/小时不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]{0,6}(\.[0-9]{1,2})?$/,
                                  message: '只允许输入整数6位,小数2位的数字',
                                  trigger: 'blur'
                              }
                          ],
                   chongdianzhuangContent: [
                              { required: true, message: '充电桩介绍不能为空', trigger: 'blur' },
                          ],
                   shangxiaTypes: [
                              { required: true, message: '是否上架不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]*$/,
                                  message: '只允许输入整数',
                                  trigger: 'blur'
                              }
                          ],
                   chongdianzhuangDelete: [
                              { required: true, message: '逻辑删除不能为空', trigger: 'blur' },
                              {  pattern: /^[0-9]*$/,
                                  message: '只允许输入整数',
                                  trigger: 'blur'
                              }
                          ],
                   insertTime: [
                              { required: true, message: '录入时间不能为空', trigger: 'blur' },
                          ],
                },
                // 地图相关
                searchAddress: '', // 搜索地址
                map: null, // 地图实例
                marker: null, // 标记
                geocoder: null, // 地理编码
            };
        },
        props: ["parent"],
        computed: {
        },
        created() {
            //获取当前登录用户的信息
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");


            if (this.role != "管理员"){
            }
            this.addEditForm = styleJs.addStyle();
            this.addEditStyleChange()
            this.addEditUploadStyleChange()
            //获取下拉框信息
                this.$http({
                    url:`dictionary/page?page=1&limit=100&sort=&order=&dicCode=chongdianzhuang_types`,
                    method: "get"
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.chongdianzhuangTypesOptions = data.data.list;
                    }
                });
                this.$http({
                    url:`dictionary/page?page=1&limit=100&sort=&order=&dicCode=chongdianzhuang_zhuangtai_types`,
                    method: "get"
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.chongdianzhuangZhuangtaiTypesOptions = data.data.list;
                    }
                });
                this.$http({
                    url:`dictionary/page?page=1&limit=100&sort=&order=&dicCode=shangxia_types`,
                    method: "get"
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        this.shangxiaTypesOptions = data.data.list;
                    }
                });


        },
        mounted() {
            // 初始化地图
            this.$nextTick(() => {
                this.initMap();
            });
        },
        methods: {
            // 下载
            download(file){
                window.open(`${file}`)
            },
            // 初始化
            init(id,type) {
                if (id) {
                    this.id = id;
                    this.type = type;
                }
                if(this.type=='info'||this.type=='else'){
                    this.info(id);
                }
                // 获取用户信息
                this.$http({
                    url:`${this.$storage.get("sessionTable")}/session`,
                    method: "get"
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        var json = data.data;
                    } else {
                        this.$message.error(data.msg);
                    }
                });
            },
            // 多级联动参数
            info(id) {
                let _this =this;
                _this.$http({
                    url: `chongdianzhuang/info/${id}`,
                    method: 'get'
                }).then(({ data }) => {
                    if (data && data.code === 0) {
                        _this.ruleForm = data.data;
                        _this.ruleForm.chongdianzhuangContent = _this.ruleForm.chongdianzhuangContent.replaceAll("src=\"upload/","src=\""+this.$base.url+"upload/");
                        // 确保开关字段有默认值
                        if (_this.ruleForm.isFastCharge === null || _this.ruleForm.isFastCharge === undefined) {
                            _this.ruleForm.isFastCharge = 0;
                        }
                        if (_this.ruleForm.isFreeParking === null || _this.ruleForm.isFreeParking === undefined) {
                            _this.ruleForm.isFreeParking = 0;
                        }
                    } else {
                        _this.$message.error(data.msg);
                    }
                });
            },
            // 提交
            onSubmit() {
                this.$refs["ruleForm"].validate(valid => {
                    if (valid) {
                        // 确保地理位置信息有效
                        if (!this.ruleForm.longitude || !this.ruleForm.latitude || !this.ruleForm.address) {
                            this.$message.warning('请选择充电桩位置');
                            return;
                        }
                        
                        this.ruleForm.chongdianzhuangContent = this.ruleForm.chongdianzhuangContent.replaceAll(this.$base.url,"");
                        this.$http({
                            url:`chongdianzhuang/${!this.ruleForm.id ? "save" : "update"}`,
                            method: "post",
                            data: this.ruleForm
                        }).then(({ data }) => {
                            if (data && data.code === 0) {
                                this.$message({
                                    message: "操作成功",
                                    type: "success",
                                    duration: 1500,
                                    onClose: () => {
                                        this.parent.showFlag = true;
                                        this.parent.addOrUpdateFlag = false;
                                        this.parent.chongdianzhuangCrossAddOrUpdateFlag = false;
                                        this.parent.search();
                                        this.parent.contentStyleChange();
                                    }
                                });
                            } else {
                                this.$message.error(data.msg);
                            }
                        });
                    }
                });
            },
            // 获取uuid
            getUUID () {
                return new Date().getTime();
            },
            // 返回
            back() {
                this.parent.showFlag = true;
                this.parent.addOrUpdateFlag = false;
                this.parent.chongdianzhuangCrossAddOrUpdateFlag = false;
                this.parent.contentStyleChange();
            },
            //图片
            chongdianzhuangPhotoUploadChange(fileUrls){
                this.ruleForm.chongdianzhuangPhoto = fileUrls;
                this.addEditUploadStyleChange()
            },

            addEditStyleChange() {
                this.$nextTick(()=>{
                    // input
                    document.querySelectorAll('.addEdit-block .input .el-input__inner').forEach(el=>{
                        el.style.height = this.addEditForm.inputHeight
                        el.style.color = this.addEditForm.inputFontColor
                        el.style.fontSize = this.addEditForm.inputFontSize
                        el.style.borderWidth = this.addEditForm.inputBorderWidth
                        el.style.borderStyle = this.addEditForm.inputBorderStyle
                        el.style.borderColor = this.addEditForm.inputBorderColor
                        el.style.borderRadius = this.addEditForm.inputBorderRadius
                        el.style.backgroundColor = this.addEditForm.inputBgColor
                    })
                    document.querySelectorAll('.addEdit-block .input .el-form-item__label').forEach(el=>{
                        el.style.lineHeight = this.addEditForm.inputHeight
                        el.style.color = this.addEditForm.inputLableColor
                        el.style.fontSize = this.addEditForm.inputLableFontSize
                    })
                    // select
                    document.querySelectorAll('.addEdit-block .select .el-input__inner').forEach(el=>{
                        el.style.height = this.addEditForm.selectHeight
                        el.style.color = this.addEditForm.selectFontColor
                        el.style.fontSize = this.addEditForm.selectFontSize
                        el.style.borderWidth = this.addEditForm.selectBorderWidth
                        el.style.borderStyle = this.addEditForm.selectBorderStyle
                        el.style.borderColor = this.addEditForm.selectBorderColor
                        el.style.borderRadius = this.addEditForm.selectBorderRadius
                        el.style.backgroundColor = this.addEditForm.selectBgColor
                    })
                    document.querySelectorAll('.addEdit-block .select .el-form-item__label').forEach(el=>{
                        el.style.lineHeight = this.addEditForm.selectHeight
                        el.style.color = this.addEditForm.selectLableColor
                        el.style.fontSize = this.addEditForm.selectLableFontSize
                    })
                    document.querySelectorAll('.addEdit-block .select .el-select__caret').forEach(el=>{
                        el.style.color = this.addEditForm.selectIconFontColor
                        el.style.fontSize = this.addEditForm.selectIconFontSize
                    })
                    // date
                    document.querySelectorAll('.addEdit-block .date .el-input__inner').forEach(el=>{
                        el.style.height = this.addEditForm.dateHeight
                        el.style.color = this.addEditForm.dateFontColor
                        el.style.fontSize = this.addEditForm.dateFontSize
                        el.style.borderWidth = this.addEditForm.dateBorderWidth
                        el.style.borderStyle = this.addEditForm.dateBorderStyle
                        el.style.borderColor = this.addEditForm.dateBorderColor
                        el.style.borderRadius = this.addEditForm.dateBorderRadius
                        el.style.backgroundColor = this.addEditForm.dateBgColor
                    })
                    document.querySelectorAll('.addEdit-block .date .el-form-item__label').forEach(el=>{
                        el.style.lineHeight = this.addEditForm.dateHeight
                        el.style.color = this.addEditForm.dateLableColor
                        el.style.fontSize = this.addEditForm.dateLableFontSize
                    })
                    document.querySelectorAll('.addEdit-block .date .el-input__icon').forEach(el=>{
                        el.style.color = this.addEditForm.dateIconFontColor
                        el.style.fontSize = this.addEditForm.dateIconFontSize
                        el.style.lineHeight = this.addEditForm.dateHeight
                    })
                    // upload
                    let iconLineHeight = parseInt(this.addEditForm.uploadHeight) - parseInt(this.addEditForm.uploadBorderWidth) * 2 + 'px'
                    document.querySelectorAll('.addEdit-block .upload .el-upload--picture-card').forEach(el=>{
                        el.style.width = this.addEditForm.uploadHeight
                        el.style.height = this.addEditForm.uploadHeight
                        el.style.borderWidth = this.addEditForm.uploadBorderWidth
                        el.style.borderStyle = this.addEditForm.uploadBorderStyle
                        el.style.borderColor = this.addEditForm.uploadBorderColor
                        el.style.borderRadius = this.addEditForm.uploadBorderRadius
                        el.style.backgroundColor = this.addEditForm.uploadBgColor
                    })
                    document.querySelectorAll('.addEdit-block .upload .el-form-item__label').forEach(el=>{
                        el.style.lineHeight = this.addEditForm.uploadHeight
                        el.style.color = this.addEditForm.uploadLableColor
                        el.style.fontSize = this.addEditForm.uploadLableFontSize
                    })
                    document.querySelectorAll('.addEdit-block .upload .el-icon-plus').forEach(el=>{
                        el.style.color = this.addEditForm.uploadIconFontColor
                        el.style.fontSize = this.addEditForm.uploadIconFontSize
                        el.style.lineHeight = iconLineHeight
                        el.style.display = 'block'
                    })
                    // 多文本输入框
                    document.querySelectorAll('.addEdit-block .textarea .el-textarea__inner').forEach(el=>{
                        el.style.height = this.addEditForm.textareaHeight
                        el.style.color = this.addEditForm.textareaFontColor
                        el.style.fontSize = this.addEditForm.textareaFontSize
                        el.style.borderWidth = this.addEditForm.textareaBorderWidth
                        el.style.borderStyle = this.addEditForm.textareaBorderStyle
                        el.style.borderColor = this.addEditForm.textareaBorderColor
                        el.style.borderRadius = this.addEditForm.textareaBorderRadius
                        el.style.backgroundColor = this.addEditForm.textareaBgColor
                    })
                    document.querySelectorAll('.addEdit-block .textarea .el-form-item__label').forEach(el=>{
                        // el.style.lineHeight = this.addEditForm.textareaHeight
                        el.style.color = this.addEditForm.textareaLableColor
                        el.style.fontSize = this.addEditForm.textareaLableFontSize
                    })
                    // 保存
                    document.querySelectorAll('.addEdit-block .btn .btn-success').forEach(el=>{
                        el.style.width = this.addEditForm.btnSaveWidth
                        el.style.height = this.addEditForm.btnSaveHeight
                        el.style.color = this.addEditForm.btnSaveFontColor
                        el.style.fontSize = this.addEditForm.btnSaveFontSize
                        el.style.borderWidth = this.addEditForm.btnSaveBorderWidth
                        el.style.borderStyle = this.addEditForm.btnSaveBorderStyle
                        el.style.borderColor = this.addEditForm.btnSaveBorderColor
                        el.style.borderRadius = this.addEditForm.btnSaveBorderRadius
                        el.style.backgroundColor = this.addEditForm.btnSaveBgColor
                    })
                    // 返回
                    document.querySelectorAll('.addEdit-block .btn .btn-close').forEach(el=>{
                        el.style.width = this.addEditForm.btnCancelWidth
                        el.style.height = this.addEditForm.btnCancelHeight
                        el.style.color = this.addEditForm.btnCancelFontColor
                        el.style.fontSize = this.addEditForm.btnCancelFontSize
                        el.style.borderWidth = this.addEditForm.btnCancelBorderWidth
                        el.style.borderStyle = this.addEditForm.btnCancelBorderStyle
                        el.style.borderColor = this.addEditForm.btnCancelBorderColor
                        el.style.borderRadius = this.addEditForm.btnCancelBorderRadius
                        el.style.backgroundColor = this.addEditForm.btnCancelBgColor
                    })
                })
            },
            addEditUploadStyleChange() {
                this.$nextTick(()=>{
                    document.querySelectorAll('.addEdit-block .upload .el-upload-list--picture-card .el-upload-list__item').forEach(el=>{
                        el.style.width = this.addEditForm.uploadHeight
                        el.style.height = this.addEditForm.uploadHeight
                        el.style.borderWidth = this.addEditForm.uploadBorderWidth
                        el.style.borderStyle = this.addEditForm.uploadBorderStyle
                        el.style.borderColor = this.addEditForm.uploadBorderColor
                        el.style.borderRadius = this.addEditForm.uploadBorderRadius
                        el.style.backgroundColor = this.addEditForm.uploadBgColor
                    })
                })
            },
            
            // 初始化地图
            initMap() {
                // 确保地图容器已加载
                if (!document.getElementById('mapContainer')) {
                    console.error('地图容器不存在');
                    return;
                }

                // 添加高德地图安全配置
                window._AMapSecurityConfig = {
                    securityJsCode: '3c76d5b216dc987d2a946db9d90d3153',
                };

                // 加载高德地图脚本
                const script = document.createElement('script');
                script.src = 'https://webapi.amap.com/maps?v=2.0&key=975982151a6f2a3f0bee6c2e0b0a0d50&plugin=AMap.Geocoder';
                script.async = true;
                script.onload = () => {
                    this.createMap();
                };
                document.head.appendChild(script);
            },

            // 创建地图实例
            createMap() {
                this.map = new AMap.Map('mapContainer', {
                    resizeEnable: true,
                    zoom: 11
                });
                
                // 创建地理编码实例
                AMap.plugin(['AMap.Geocoder'], () => {
                    this.geocoder = new AMap.Geocoder();
                    
                    // 如果已有经纬度，则在地图上标记
                    if (this.ruleForm.longitude && this.ruleForm.latitude) {
                        const position = new AMap.LngLat(this.ruleForm.longitude, this.ruleForm.latitude);
                        this.addMarker(position);
                        this.map.setCenter(position);
                        this.map.setZoom(15);
                    }
                });
                
                // 添加点击地图事件
                this.map.on('click', (e) => {
                    const lngLat = e.lnglat;
                    this.addMarker(lngLat);
                    this.updateLocationInfo(lngLat);
                });
            },
            
            // 添加标记
            addMarker(position) {
                // 如果已有标记，先移除
                if (this.marker) {
                    this.map.remove(this.marker);
                }
                
                // 创建新标记
                this.marker = new AMap.Marker({
                    position: position,
                    draggable: true, // 可拖动
                    title: '充电桩位置'
                });
                
                // 拖动结束后更新位置信息
                this.marker.on('dragend', (e) => {
                    const newPos = e.target.getPosition();
                    this.updateLocationInfo(newPos);
                });
                
                this.map.add(this.marker);
            },
            
            // 更新位置信息
            updateLocationInfo(lngLat) {
                this.ruleForm.longitude = lngLat.getLng();
                this.ruleForm.latitude = lngLat.getLat();
                
                // 反向地理编码，获取地址
                if (this.geocoder) {
                    this.geocoder.getAddress(lngLat, (status, result) => {
                        if (status === 'complete' && result.info === 'OK') {
                            this.ruleForm.address = result.regeocode.formattedAddress;
                        } else {
                            this.$message.error('获取地址失败');
                        }
                    });
                }
            },
            
            // 搜索位置
            searchLocation() {
                if (!this.searchAddress) {
                    this.$message.warning('请输入搜索地址');
                    return;
                }
                
                if (this.geocoder) {
                    this.geocoder.getLocation(this.searchAddress, (status, result) => {
                        if (status === 'complete' && result.info === 'OK' && result.geocodes.length > 0) {
                            const location = result.geocodes[0].location;
                            this.addMarker(location);
                            this.map.setCenter(location);
                            this.map.setZoom(15);
                            this.updateLocationInfo(location);
                        } else {
                            this.$message.error('地址搜索失败');
                        }
                    });
                } else {
                    this.$message.error('地理编码服务未初始化');
                }
            }
        }
    };
</script>
<style lang="scss">
.editor{
  height: 500px;

  ::v-deep .ql-container {
	  height: 310px;
  }
}
.amap-wrapper {
  width: 100%;
  height: 500px;
}
.search-box {
  position: absolute;
}
.addEdit-block {
	margin: -10px;
}
.detail-form-content {
	padding: 12px;
}
.btn .el-button {
  padding: 0;
}

.map-container {
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}
</style>

