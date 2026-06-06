-- 将所有充电桩的录入时间更新为2026年3月份
-- 使用不同的日期来模拟真实数据的时间分布

UPDATE chongdianzhuang SET insert_time = '2026-03-01 08:30:00' WHERE id = 1;
UPDATE chongdianzhuang SET insert_time = '2026-03-02 10:15:00' WHERE id = 2;
UPDATE chongdianzhuang SET insert_time = '2026-03-05 14:20:00' WHERE id = 3;
UPDATE chongdianzhuang SET insert_time = '2026-03-08 09:45:00' WHERE id = 4;
UPDATE chongdianzhuang SET insert_time = '2026-03-10 16:30:00' WHERE id = 5;
UPDATE chongdianzhuang SET insert_time = '2026-03-12 11:00:00' WHERE id = 6;
UPDATE chongdianzhuang SET insert_time = '2026-03-15 08:00:00' WHERE id = 7;
UPDATE chongdianzhuang SET insert_time = '2026-03-18 13:45:00' WHERE id = 8;
UPDATE chongdianzhuang SET insert_time = '2026-03-20 10:30:00' WHERE id = 9;
UPDATE chongdianzhuang SET insert_time = '2026-03-22 15:15:00' WHERE id = 10;
UPDATE chongdianzhuang SET insert_time = '2026-03-25 09:00:00' WHERE id = 11;
UPDATE chongdianzhuang SET insert_time = '2026-03-28 12:00:00' WHERE id = 12;

-- 如果有更多充电桩，使用批量更新（将所有剩余记录更新为3月中旬）
UPDATE chongdianzhuang SET insert_time = DATE_ADD('2026-03-01', INTERVAL FLOOR(RAND() * 30) DAY)
WHERE id NOT IN (1,2,3,4,5,6,7,8,9,10,11,12);

-- 验证更新结果
SELECT id, chongdianzhuang_name, insert_time FROM chongdianzhuang ORDER BY insert_time;
