-- 为每个充电桩添加快充和慢充电枪，并添加描述
-- 执行前请先备份数据库！

-- ============================================
-- 第一步：为充电桩添加描述
-- ============================================

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于市中心繁华地段，24小时营业，配备专业客服团队，提供免费WiFi和休息区。充电桩采用最新技术，充电速度快，安全可靠。支持微信、支付宝等多种支付方式。' WHERE id = 1;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于商业广场地下停车场，交通便利，周边配套设施齐全。充电站环境整洁，配备消防设施，安全有保障。提供快速充电服务，适合紧急补能需求。' WHERE id = 2;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于高速公路服务区，专为长途驾驶设计，提供餐饮、休息等综合服务。充电桩数量充足，支持多种车型，是长途出行的理想补能站点。' WHERE id = 3;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于居民小区附近，服务周边居民日常充电需求。充电站安静舒适，配备遮阳棚，雨天充电无忧。价格实惠，是家庭充电的首选。' WHERE id = 4;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于工业园区，服务园区内企业和员工。充电站容量大，可同时满足多辆电动车充电需求。支持预约充电，避免排队等待。' WHERE id = 5;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于购物中心停车场，购物充电两不误。充电站位置显眼，易于寻找。提供会员优惠，长期充电更划算。' WHERE id = 6;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于医院附近，方便就医人员充电。充电站24小时运营，安全可靠。配备紧急呼叫按钮，遇到问题可及时求助。' WHERE id = 7;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于学校周边，服务教职工和学生家长。充电站环境安全，有专人管理。提供慢充服务，价格优惠，适合长时间停车充电。' WHERE id = 8;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于旅游景区，服务游客充电需求。充电站风景优美，充电时可欣赏美景。提供旅游咨询服务，让您的旅程更加便捷。' WHERE id = 9;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于物流园区，服务物流车辆充电。充电站功率大，充电速度快，适合物流车辆的快速补能需求。提供夜间充电优惠。' WHERE id = 10;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于写字楼地下停车场，服务上班族充电。充电站位置便利，上下班即可充电。提供预约服务，确保有空位可用。' WHERE id = 11;

UPDATE chongdianzhuang SET chongdianzhuang_content = '位于郊区，环境安静，充电体验佳。充电站配备休息区，可享受宁静的充电时光。价格实惠，是周末出游的好选择。' WHERE id = 12;

-- 如果有更多充电桩，使用默认描述
UPDATE chongdianzhuang SET chongdianzhuang_content = '专业充电站，提供安全、便捷的充电服务。配备先进的充电设备，支持多种充电模式。24小时营业，随时为您提供充电服务。'
WHERE chongdianzhuang_content IS NULL OR chongdianzhuang_content = '';

-- ============================================
-- 第二步：为每个充电桩添加快充电枪
-- ============================================

-- 快充：直流充电，功率60kW-120kW，接口类型为国标直流(2)

INSERT INTO charging_gun (station_id, gun_no, gun_name, gun_type, power_kw, status, create_time, update_time)
SELECT
    id,
    CONCAT('FC-', id, '-01'),
    CONCAT('快充枪01-', SUBSTRING(chongdianzhuang_name, 1, 5)),
    2,  -- 国标直流
    CASE
        WHEN id <= 4 THEN 120.0
        WHEN id <= 8 THEN 60.0
        ELSE 90.0
    END,
    1,  -- 空闲
    NOW(),
    NOW()
FROM chongdianzhuang
WHERE id NOT IN (SELECT DISTINCT station_id FROM charging_gun WHERE gun_no LIKE 'FC-%');

-- ============================================
-- 第三步：为每个充电桩添加慢充电枪
-- ============================================

-- 慢充：交流充电，功率7kW-22kW，接口类型为国标交流(1)

INSERT INTO charging_gun (station_id, gun_no, gun_name, gun_type, power_kw, status, create_time, update_time)
SELECT
    id,
    CONCAT('SC-', id, '-01'),
    CONCAT('慢充枪01-', SUBSTRING(chongdianzhuang_name, 1, 5)),
    1,  -- 国标交流
    CASE
        WHEN id <= 4 THEN 22.0
        WHEN id <= 8 THEN 7.0
        ELSE 11.0
    END,
    1,  -- 空闲
    NOW(),
    NOW()
FROM chongdianzhuang
WHERE id NOT IN (SELECT DISTINCT station_id FROM charging_gun WHERE gun_no LIKE 'SC-%');

-- ============================================
-- 第四步：验证结果
-- ============================================

-- 查看充电桩数量
SELECT COUNT(*) as '充电桩总数' FROM chongdianzhuang WHERE shangxia_types = 1;

-- 查看每个充电桩的充电枪数量
SELECT
    c.id as '充电桩ID',
    c.chongdianzhuang_name as '充电桩名称',
    COUNT(g.id) as '充电枪数量',
    SUM(CASE WHEN g.gun_type = 2 THEN 1 ELSE 0 END) as '快充枪数量',
    SUM(CASE WHEN g.gun_type = 1 THEN 1 ELSE 0 END) as '慢充枪数量'
FROM chongdianzhuang c
LEFT JOIN charging_gun g ON c.id = g.station_id
WHERE c.shangxia_types = 1
GROUP BY c.id, c.chongdianzhuang_name
ORDER BY c.id;

-- 查看充电枪详情
SELECT
    g.id as '充电枪ID',
    g.station_id as '充电桩ID',
    c.chongdianzhuang_name as '充电桩名称',
    g.gun_no as '枪头编号',
    g.gun_name as '枪头名称',
    CASE g.gun_type
        WHEN 1 THEN '国标交流'
        WHEN 2 THEN '国标直流'
        WHEN 3 THEN '欧标'
        WHEN 4 THEN '特斯拉'
        ELSE '未知'
    END as '接口类型',
    g.power_kw as '功率(kW)',
    CASE g.status
        WHEN 1 THEN '空闲'
        WHEN 2 THEN '占用'
        WHEN 3 THEN '故障'
        WHEN 4 THEN '离线'
        ELSE '未知'
    END as '状态',
    g.create_time as '创建时间'
FROM charging_gun g
LEFT JOIN chongdianzhuang c ON g.station_id = c.id
ORDER BY g.station_id, g.gun_no;

-- 查看充电桩描述
SELECT
    id as '充电桩ID',
    chongdianzhuang_name as '充电桩名称',
    chongdianzhuang_content as '描述'
FROM chongdianzhuang
WHERE shangxia_types = 1
ORDER BY id;