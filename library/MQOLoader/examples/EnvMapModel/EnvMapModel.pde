// EnvMapModel Sample / Written by n_ryota ( http://cafe.eyln.com )
import eyln.mqoloader.*;

MQOModel model; // MQOモデル
PImage bg, mirrorMap, celMap, gradationMap;
PVector cameraPos, cameraTargetPos;
boolean lightEnable = false;
int bgColor = 32;
int mode = 0;
String modeGuide = "Wireframe";

// 初期化
void setup() {
  size(512, 512, P3D); // 3Dモードで画面を初期化

  model = new MQOModel(this, "apple.mqo");

  bg = loadImage("bg.jpg");
  mirrorMap = loadImage("envmap.png");
  celMap = loadImage("celmap.png");
  gradationMap = loadImage("gradationmap.png");
  model.setShading(MQOModel.SHADING_WIREFRAME);

  cameraPos = new PVector(0, 0, 350);
  cameraTargetPos = new PVector(0, 0, 0);
}

// スペースキーかマウスクリックでモデルの描画モードを変更
void keyPressed() {
  if(key==' ') stepMode();
}
void mouseClicked() {
  stepMode();
}

// モデルの描画モードを1つ変更(順番に見せる)
void stepMode() {
  mode = (mode + 1) % 13;
  switch(mode) {
  case 0: modeGuide = "Wireframe";
    model.noEnvMap(); bgColor = 32; lightEnable=false;
    model.setShading(MQOModel.SHADING_WIREFRAME);
    break;
  case 1: modeGuide = "Flat Shading / Light Off";
    lightEnable = false;
    model.setShading(MQOModel.SHADING_FLAT);
    break;
  case 2: modeGuide = "Flat Shading / Light On";
    lightEnable = true;
    model.setShading(MQOModel.SHADING_FLAT);
    break;
  case 3: modeGuide = "Gouraud Shading / Light On";
    lightEnable = true;
    model.setShading(MQOModel.SHADING_GOURAUD);
    break;
  case 4: modeGuide = "Gradation Mapping / Light Off";
    lightEnable = false;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR, gradationMap, color(255, 200));
    break;
  case 5: modeGuide = "Cel Mapping / Light Off";
    lightEnable = false; bgColor = 128;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR, celMap, color(255, 170));
    break;
  case 6: modeGuide = "Cel Only Mapping / Light Off";
    lightEnable = false;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR_ONLY, celMap, color(255, 128));
    break;
  case 7: modeGuide = "Mirror Mapping (Reflection) / Light On";
    lightEnable = true; bgColor = 32;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR, mirrorMap, color(255, 64));
    break;
  case 8: modeGuide = "Mirror Mapping Gold / Light Off";
    lightEnable = false;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR_ONLY, mirrorMap, color(240, 180, 0, 255));
    break;
  case 9: modeGuide = "Mirror Mapping Silver / Light Off";
    lightEnable = false;
    model.setEnvMap(MQOModel.ENVMAP_MIRROR_ONLY, mirrorMap, color(255, 255));
    break;
  case 10: modeGuide = "Glass Mapping (Reflaction) / Light Off";
    lightEnable = false; bgColor = 0;
    model.setEnvMap(MQOModel.ENVMAP_GLASS, bg);
    break;
  case 11: modeGuide = "Glass Reverse Mapping / Light Off";
    lightEnable = false;
    model.setEnvMap(MQOModel.ENVMAP_GLASS_REVERSE, bg);
    break;
  case 12: modeGuide = "Flat Shading / Glass Mapping / Light On";
    lightEnable=true;
    model.setShading(MQOModel.SHADING_FLAT);
    model.setEnvMap(MQOModel.ENVMAP_GLASS, bg);
    break;
  }
}

// 描画
void draw() {
  background(bgColor);

  if(keyPressed) {
     if(keyCode==SHIFT) {
      cameraPos.z = mouseY;
    } else if(keyCode==CONTROL) {
      cameraTargetPos.x = cameraPos.x = -mouseX + width/2;
      cameraTargetPos.y = cameraPos.y = -mouseY + height/2;
    }
  }

  camera(cameraPos.x, cameraPos.y, cameraPos.z, cameraTargetPos.x, cameraTargetPos.y, cameraTargetPos.z, 0,1,0);
  perspective(PI/3.0, (float)width/(float)height, 1, 5000);

  if(lightEnable) {
    ambientLight(80, 80, 80); // 環境光
    directionalLight(255, 255, 255, 0.5f, 0.5f, -0.5f); // 平行光源
  } else {
    noLights();
  }

  pushMatrix();
    rotateY( radians( -180.0f + 360.0f * mouseX / width ) );
    rotateX( radians( 90.0f - 180.0f * mouseY / height ) );
    noStroke();
    model.draw();
  popMatrix();

  camera();
  perspective();
  noLights();
  if(bgColor==0) image(bg, 0, 0, width, height);
  fill(0, 200);
  ellipse(width/2, height, width*1.2f, 120);
  fill(255);
  textMode(SCREEN);
  textAlign(CENTER); text(modeGuide, width/2, height-25);
  //textAlign(LEFT); text(nf(frameRate, 2, 1) + "fps", 10, height-15);
}
