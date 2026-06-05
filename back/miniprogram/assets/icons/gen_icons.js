/**
 * 生成带真实图标形状的 PNG（RGBA，透明背景）
 * 纯 Node.js，无依赖
 */
const fs = require('fs');
const path = require('path');
const zlib = require('zlib');

const SIZE = 81; // 奇数方便居中

// ---- PNG 编码工具 ----
function crc32(buf) {
  let crc = 0xffffffff;
  for (const b of buf) { crc ^= b; for (let i = 0; i < 8; i++) crc = (crc >>> 1) ^ (crc & 1 ? 0xedb88320 : 0); }
  return (crc ^ 0xffffffff) >>> 0;
}
function pngChunk(type, data) {
  const t = Buffer.from(type), len = Buffer.alloc(4), crcBuf = Buffer.alloc(4);
  len.writeUInt32BE(data.length);
  crcBuf.writeUInt32BE(crc32(Buffer.concat([t, data])));
  return Buffer.concat([len, t, data, crcBuf]);
}
function makePNG(pixels) { // pixels: SIZE*SIZE 个 [r,g,b,a]
  const sig = Buffer.from([137,80,78,71,13,10,26,10]);
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(SIZE,0); ihdr.writeUInt32BE(SIZE,4);
  ihdr[8]=8; ihdr[9]=6; // RGBA
  const raw = [];
  for (let y = 0; y < SIZE; y++) {
    raw.push(0);
    for (let x = 0; x < SIZE; x++) {
      const p = pixels[y * SIZE + x];
      raw.push(p[0], p[1], p[2], p[3]);
    }
  }
  const idat = zlib.deflateSync(Buffer.from(raw));
  return Buffer.concat([sig, pngChunk('IHDR',ihdr), pngChunk('IDAT',idat), pngChunk('IEND',Buffer.alloc(0))]);
}

// ---- 像素画布工具 ----
function newCanvas() {
  return Array.from({length: SIZE * SIZE}, () => [0,0,0,0]);
}
// 填充圆形
function fillCircle(px, cx, cy, r, color) {
  for (let y = 0; y < SIZE; y++) for (let x = 0; x < SIZE; x++) {
    if ((x-cx)**2 + (y-cy)**2 <= r*r) px[y*SIZE+x] = color;
  }
}
// 填充矩形
function fillRect(px, x1, y1, x2, y2, color) {
  for (let y = y1; y <= y2; y++) for (let x = x1; x <= x2; x++) {
    if (x>=0&&x<SIZE&&y>=0&&y<SIZE) px[y*SIZE+x] = color;
  }
}
// 填充三角形（扫描线）
function fillTriangle(px, x1,y1, x2,y2, x3,y3, color) {
  const minY = Math.max(0, Math.min(y1,y2,y3));
  const maxY = Math.min(SIZE-1, Math.max(y1,y2,y3));
  for (let y = minY; y <= maxY; y++) {
    const xs = [];
    [[x1,y1,x2,y2],[x2,y2,x3,y3],[x3,y3,x1,y1]].forEach(([ax,ay,bx,by]) => {
      if ((ay<=y&&by>y)||(by<=y&&ay>y)) xs.push(ax + (y-ay)*(bx-ax)/(by-ay));
    });
    if (xs.length >= 2) { xs.sort((a,b)=>a-b); fillRect(px, Math.round(xs[0]),y,Math.round(xs[xs.length-1]),y, color); }
  }
}

const C = Math.floor(SIZE/2); // 中心点 = 40

// ---- 图标定义 ----
function drawHome(color) {
  const px = newCanvas();
  // 屋顶三角形
  fillTriangle(px, C,8, 8,38, SIZE-9,38, color);
  // 门洞（挖空）
  fillTriangle(px, C,16, 14,38, SIZE-15,38, [0,0,0,0]);
  // 墙体
  fillRect(px, 14,36, SIZE-15,SIZE-10, color);
  // 门
  fillRect(px, C-8,50, C+8,SIZE-10, [0,0,0,0]);
  return px;
}

function drawMap(color) {
  const px = newCanvas();
  // 水滴形：上半圆 + 下三角
  fillCircle(px, C, 28, 20, color);
  fillTriangle(px, C-20,38, C+20,38, C,SIZE-8, color);
  // 中心挖空小圆
  fillCircle(px, C, 28, 9, [0,0,0,0]);
  return px;
}

function drawRoute(color) {
  const px = newCanvas();
  // 闪电/充电符号
  fillTriangle(px, C+12,8, C-4,C+4, C+6,C+4, color);
  fillTriangle(px, C-12,SIZE-8, C+4,C-4, C-6,C-4, color);
  return px;
}

function drawProfile(color) {
  const px = newCanvas();
  // 头部圆
  fillCircle(px, C, 24, 14, color);
  // 身体半椭圆
  for (let y = 42; y < SIZE-6; y++) {
    const half = Math.round(22 * Math.sqrt(Math.max(0, 1 - ((y-42)/28)**2)));
    fillRect(px, C-half, y, C+half, y, color);
  }
  return px;
}

// ---- 生成文件 ----
const GRAY  = [153,153,153,255];
const GREEN = [76,175,80,255];

const icons = [
  { name: 'home',    draw: drawHome },
  { name: 'map',     draw: drawMap },
  { name: 'route',   draw: drawRoute },
  { name: 'profile', draw: drawProfile },
];

for (const { name, draw } of icons) {
  fs.writeFileSync(path.join(__dirname, `${name}.png`),        makePNG(draw(GRAY)));
  fs.writeFileSync(path.join(__dirname, `${name}-active.png`), makePNG(draw(GREEN)));
  console.log(`✓ ${name}.png / ${name}-active.png`);
}
console.log('全部完成');
