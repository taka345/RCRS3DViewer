// HelloModel p5class Sample / Written by n_ryota ( http://cafe.eyln.com )
//import eyln.mqoloader.*;

MQOModel model;

void setup() {
  size(400, 400, P3D);
  //model = new MQOModel(this, "apple.mqo");
  model = new MQOModel("apple.mqo");
}

void draw() {
  background(64);
  translate(width/2, height/2);
  rotateY( radians( -180.0f + 360.0f * mouseX / width ) );
  rotateX( radians( 90.0f - 180.0f * mouseY / height ) );
  model.draw();
}
