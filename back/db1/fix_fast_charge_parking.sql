-- ============================================
-- 修复快充和免费停车字段脚本
-- ============================================

-- 1. 检查并添加字段（如果不存在）
-- 注意：如果字段已存在，这些语句会报错，可以忽略

-- 添加 is_fast_charge 字段
ALTER TABLE chongdianzhuang 
ADD COLUMN IF NOT EXISTS is_fast_charge tinyint(1) DEFAULT 0 COMMENT '是否快充 0否1是';

-- 添加 is_free_parking 字段  
ALTER TABLE chongdianzhuang 
ADD COLUMN IF NOT EXISTS is_free_parking tinyint(1) DEFAULT 0 COMMENT '是否免费停车 0否1是';

-- 2. 更新现有数据，将 NULL 值设置为 0
UPDATE chongdianzhuang SET is_fast_charge = 0 WHERE is_fast_charge IS NULL;
UPDATE chongdianzhuang SET is_free_parking = 0 WHERE is_free_parking IS NULL;

-- 3. 查看当前数据状态
SELECT id, chongdianzhuang_name, chongdianzhuang_types, 
       is_fast_charge, is_free_parking,
       shangxia_types, chongdianzhuang_delete,
       longitude, latitude
FROM chongdianzhuang
WHERE shangxia_types = 1;

-- ============================================
-- 如果你想测试筛选功能，可以手动设置一些数据：
-- ============================================

-- 将充电桩类型3的所有记录设置为快充和免费停车
UPDATE chongdianzhuang 
SET is_fast_charge = 1, is_free_parking = 1 
WHERE chongdianzhuang_types = 3 AND shangxia_types = 1;

-- 或者单独设置某个充电桩（例如 ID=6）
-- UPDATE chongdianzhuang SET is_fast_charge = 1, is_free_parking = 1 WHERE id = 6;

-- 验证更新结果
SELECT id, chongdianzhuang_name, chongdianzhuang_types, 
       is_fast_charge, is_free_parking
FROM chongdianzhuang
WHERE chongdianzhuang_types = 3;