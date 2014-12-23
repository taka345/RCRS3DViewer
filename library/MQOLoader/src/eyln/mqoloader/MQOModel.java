/**
 * MQO Loader - loads, displays, MQO models for Processing
 *
 * Copyright (c) 2011 Ryota Nishida ( http://cafe.eyln.com/ )
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the MIT License (MIT).
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the MIT License (MIT) for more details.
 *
 * http://sourceforge.jp/projects/opensource/wiki/licenses%2FMIT_license
 * http://www.opensource.org/licenses/mit-license.php
 * 
 * MQO FileFormat
 * http://www.metaseq.net/metaseq/format.html
 * 
 * @author		Ryota Nishida http://cafe.eyln.com/
 * @modified	11/12/2011
 * @version		0.2
 */

package eyln.mqoloader;


import processing.core.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * @example HelloModel 
 *
 */

public class MQOModel {
	
	// -------- �����f�[�^�N���X
	class MQOTex {
	  String name;
	  PImage texture;
	  MQOTex(String name) {
	    texture = p.loadImage(name);
	    //p.println("texture " + name);
	  }
	}

	class MQOMaterial {
	  String name;
	  //byte shader;
	  boolean vcol;
	  int col;
	  int dif, amb, emi, spc;
	  float power;
	  MQOTex tex;
	  //MQOTex aplane;
	  //MQOTex bump;
	}

	class MQOFace {
	  int index[];
	  float u[];
	  float v[];
	  byte materialIndex;
	  byte num;
	}

	class MQOObject {
	  String name;
	  byte depth;
	  //int folding;
	  PVector scale;
	  PVector rotation;
	  PVector translation;
	  //int patch;
	  //int segment;
	  boolean visible;
	  //boolean locking;
	  byte shading; // 0 �t���b�g�V�F�[�f�B���O 1 �O���[�V�F�[�f�B���O
	  float facet;
	  int col;
	  //byte int_type;
	  //byte mirror;
	  //byte mirror_axis;
	  //float mirror_dis;
	  //int lathe;
	  //int lathe_axis;
	  //int lathe_seg;
	  PVector vertices[];
	  PVector normals[];
	  MQOFace faces[];

	  PVector getFaceNormal(int faceIndex) {
	    MQOFace f = faces[faceIndex];
	    PVector a = vertices[f.index[0]];
	    PVector b = vertices[f.index[1]];
	    PVector c = vertices[f.index[2]];
	    PVector n = PVector.sub(a, b).cross(PVector.sub(c, b));
	    n.normalize();
	    return n;
	  }
	}
	// --------

	// myParent is a reference to the parent sketch
	private PApplet p;

	private final static String VERSION = "0.2";

	private MQOMaterial materials[];
    private ArrayList<MQOObject> objects;

    private byte shadingMode;
    public final static byte SHADING_FLAT = 0;          // �t���b�g�V�F�[�f�B���O
    public final static byte SHADING_GOURAUD = 1;       // �O���[�V�F�[�f�B���O
    public final static byte SHADING_WIREFRAME = 2;     // ���C���[�t���[��(MQO�ɂ͂Ȃ�)
    public final static byte SHADING_AUTO = -1;         // �I�u�W�F�N�g�̐ݒ�l���g�p

    private PImage envMap;                              // ���}�b�v�p�e�N�X�`��
    private int envMapColor;                            // ���}�b�v�F
    private byte envMapMode;
    public final static byte ENVMAP_NONE = 0;           // ���}�b�v�Ȃ�
    public final static byte ENVMAP_MIRROR = 1;         // �����˃}�b�s���O(REFLECTION)
    public final static byte ENVMAP_MIRROR_ONLY = 2;    // �����˃}�b�s���O�݂̂ŃI�u�W�F�N�g�`��
    public final static byte ENVMAP_GLASS = 3;          // ����܃}�b�s���O(REFLACTION)
    public final static byte ENVMAP_GLASS_REVERSE = 4;  // ����܃}�b�s���O UV���]��

	/**
	 * @param theParent
	 */
    public MQOModel(PApplet theParent) {
	  p = theParent;
    }

    /**
	 * @example HelloModel
	 * @param theParent
	 * @param file
	 */
    public MQOModel(PApplet theParent, String file) {
	  p = theParent;
	  load(file);
    }

	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public static String version() {
	  return VERSION;
	}

    // ���f���ǂݍ���
	/**
	 * @param file
	 *          mqo filename
	 */
    public void load(String file) {
      String[] lines = p.loadStrings(file);
      int cur = parseMaterialChunk(lines, 0);
      
      objects = new ArrayList<MQOObject>();
      while(cur>=0) {
        cur = parseObjectChunk(lines, cur);
      }
      
      computeNormals();
      sortFaces();
      shadingMode = SHADING_AUTO;
      envMap = null;
      envMapColor = p.color(0);
      envMapMode = ENVMAP_NONE;
    }

    // �V�F�[�f�B���O�^�C�v�w��(SHADING_*����)
    public void setShading(byte shading) {
      shadingMode = shading;
    }

    // ���}�b�v�w��
    public void setEnvMap(byte envMapMode, PImage texture) {
      setEnvMap(envMapMode, texture, p.color(255));
    }
    public void setEnvMap(byte envMapMode, PImage texture, int envMapColor) {
      envMap = texture;
      this.envMapColor = envMapColor;
      this.envMapMode = envMapMode;
    }
    public void noEnvMap() {
      setEnvMap(ENVMAP_NONE, null, p.color(0));
    }

    // �`��
    public void draw() {
      if(objects==null) return;

      p.pushStyle();

      for (int i=0; i<objects.size(); i++) {
        MQOObject o = objects.get(i);

        // ���[�J�����W�Ȃǂ�ݒ�
        p.pushMatrix();
        p.translate(o.translation.x, o.translation.y, o.translation.z);
        p.scale(o.scale.x, o.scale.y, o.scale.z);
        p.rotateY(o.rotation.y); p.rotateX(o.rotation.x); p.rotateZ(o.rotation.z); // HPB��]

        // �I�u�W�F�N�g�`��
        if(  envMapMode!=ENVMAP_GLASS && envMapMode!=ENVMAP_GLASS_REVERSE && envMapMode!=ENVMAP_MIRROR_ONLY ) {
          drawObject(o, ENVMAP_NONE);
        }
        if(envMapMode!=ENVMAP_NONE) {
          drawObject(o, envMapMode);
        }

        p.popMatrix();
      }

      p.popStyle();
    }

    // �I�u�W�F�N�g�`��
    private void drawObject(MQOObject o, byte envMapMode) {
      PMatrix3D matrix = new PMatrix3D();
      p.getMatrix(matrix);
      PVector cameraPos = new PVector(-matrix.m03, -matrix.m13, -matrix.m23);
      matrix.m03 = matrix.m13 = matrix.m23 = 0.0f; // 3x3�ł悢

      // �V�F�[�f�B���O���[�h��ݒ�
      byte nowShading = (shadingMode==SHADING_AUTO) ? o.shading : shadingMode;
      if(nowShading==SHADING_WIREFRAME) p.noFill();
      else p.noStroke();

      // �I�u�W�F�N�g���x�^�ɕ`��
      int faceVNum = -1;
      int materialIndex = -999;
      PImage img = null;
      for (int j=0; j<o.faces.length; j++) {
        MQOFace f = o.faces[j];

        // �}�e���A���Ȃǂ̐ݒ�
        if(f.materialIndex!=materialIndex || f.num!=faceVNum) {
          faceVNum = f.num;
          if(j!=0) p.endShape();
          if(f.num==3) p.beginShape(p.TRIANGLES);
          else p.beginShape(p.QUAD);

          img = null;
          materialIndex = f.materialIndex;
          if(materialIndex>=0 && materialIndex<materials.length) {
            MQOMaterial m = materials[materialIndex];
            if(nowShading==SHADING_WIREFRAME) p.stroke(m.dif);
            else {
              if(envMapMode!=ENVMAP_NONE) {
                img = envMap;
                p.texture(img);
                p.textureMode(p.NORMALIZED);
                p.tint(envMapColor);
              } else if(m.tex!=null) {
                img = m.tex.texture;
                p.texture(img);
                p.textureMode(p.NORMALIZED);
                p.noTint();
              } else {
                p.noTexture();
              }
              if(envMapMode==ENVMAP_GLASS || envMapMode==ENVMAP_GLASS_REVERSE) {
                p.fill(255);
              } else {
                p.fill(m.dif);
                p.ambient(m.amb);
                p.specular(m.spc);
                p.emissive(m.emi);
                p.shininess(m.power);
              }
            }
          }
        }

        // �|���S���`��
        PVector uvn = new PVector();
        for (int k=0; k<f.num; k++) {
          PVector v = o.vertices[f.index[k]];
          PVector n = o.normals[f.index[k]];
          if(nowShading!=SHADING_FLAT) {
            p.normal( n.x, n.y, n.z );
          }
          float tu, tv;
          if(envMapMode==ENVMAP_NONE) {
            tu = f.u[k];
            tv = f.v[k];
          } else {
            if(envMapMode==ENVMAP_MIRROR || envMapMode==ENVMAP_MIRROR_ONLY) {
              // ���˃}�b�s���O
              matrix.mult(n, uvn);
              uvn.normalize();
              uvn.x *= -1.0f;
              uvn.y *= -1.0f;
            } else {
              // ��܃}�b�s���O
              PVector lv = new PVector();
              matrix.mult(v, lv);
              PVector e = PVector.sub(cameraPos, lv);
              e.normalize();
              float factor = 0.15f;  // 0�`2
              uvn = PVector.sub( PVector.mult(n, factor * e.dot(n)), e );
              //uvn.normalize();
              if(envMapMode==ENVMAP_GLASS_REVERSE) {
                uvn.x *= -1.0f;
                uvn.y *= -1.0f;
              }
            }
            tu = uvn.x * 0.5f + 0.5f;
            tv = uvn.y * 0.5f + 0.5f;
          }
          if(img!=null) {
            p.vertex( v.x, v.y, v.z, tu, tv );
          } else {
            p.vertex( v.x, v.y, v.z );
          }
        }
      }

      if(o.faces.length>0) p.endShape();
    }

    // �`�����N�s���擾
    private int findChunkLine(String chunkName, String[] lines, int startLine) {
      for(int i=startLine; i < lines.length; i++) {
        if(lines[i].indexOf(chunkName)>=0) {
          //p.println(lines[i]);
          return i;
        }
      }
      return -1;
    }

    // ��������p�����[�^�ɕ���
    private String[] splitArg(String str) {
      return p.splitTokens(str, " \t()\"{}");
    }

    // �Y���p�����[�^index���擾
    private int findArg(String name, int valueNum, String[] args, int startArg) {
      for(int i=startArg; i<args.length; i++) {
        if(args[i].compareToIgnoreCase(name)==0 && i+valueNum<args.length) {
          return i;
        }
      }
      return -1;
    }

    // �e��p�����[�^�l���w��^�Ŏ擾
    private boolean getBoolean(String name, String[] args, int startArg, boolean defaultVal) {
      return getInt(name, args, startArg, defaultVal ? 1 : 0)!=0;
    }

    private byte getByte(String name, String[] args, int startArg, byte defaultVal) {
      int i = findArg(name, 1, args, startArg);
      if(i>=0) {
        return Byte.parseByte(args[i+1]);
      } else return defaultVal;
    }

    private int getInt(String name, String[] args, int startArg, int defaultVal) {
      int i = findArg(name, 1, args, startArg);
      if(i>=0) {
        return Integer.parseInt(args[i+1]);
      } else return defaultVal;
    }

    private float getFloat(String name, String[] args, int startArg, float defaultVal) {
      int i = findArg(name, 1, args, startArg);
      if(i>=0) {
        return Float.parseFloat(args[i+1]);
      } else return defaultVal;
    }

    private String getString(String name, String[] args, int startArg, String defaultVal) {
      int i = findArg(name, 1, args, startArg);
      if(i>=0) {
        return args[i+1];
      } else return defaultVal;
    }

    private int getColor(String name, String[] args, int startArg, int defaultVal) {
      int i = findArg(name, 3, args, startArg);
      if(i>=0) {
        return p.color(Float.parseFloat(args[i+1])*255, Float.parseFloat(args[i+2])*255, Float.parseFloat(args[i+3])*255);
      } else return defaultVal;
    }

    private int getColorGray(String name, String[] args, int startArg, int defaultVal) {
      int i = findArg(name, 1, args, startArg);
      if(i>=0) {
        return p.color(Float.parseFloat(args[i+1])*255);
      } else return defaultVal;
    }
    
    private PVector getVector(String name, String[] args, int startArg, PVector defaultVal) {
      int i = findArg(name, 3, args, startArg);
      if(i>=0) {
        return new PVector(Float.parseFloat(args[i+1]), Float.parseFloat(args[i+2]), Float.parseFloat(args[i+3]));
      } else return new PVector(defaultVal.x, defaultVal.y, defaultVal.z);
    }

    // �}�e���A���̓ǂݍ���
    private int parseMaterialChunk(String[] lines, int startLine) {
      int cur = startLine;
      cur = findChunkLine("Material", lines, cur);
      if(cur < 0) return -1;

      String[] args = splitArg(lines[cur++]);
      if(args.length <= 1) return -1;
      int num = Integer.parseInt(args[1]);
      if(num<=0) return -1;
      materials = new MQOMaterial[num];
      for(int i=0; i<num; i++) {
        args = splitArg(lines[cur++]);
        MQOMaterial m = new MQOMaterial();
        m.name = args[0];
        m.vcol = getBoolean("vcol", args, 1, false);
        m.col = getColor("col", args, 1, p.color(255));
        m.dif = p.blendColor(m.col, getColorGray("dif", args, 1, p.color(128)), p.MULTIPLY);
        m.amb = p.blendColor(m.col, getColorGray("amb", args, 1, p.color(128)), p.MULTIPLY);
        m.emi = p.blendColor(m.col, getColorGray("emi", args, 1, p.color(0)), p.MULTIPLY);
        m.spc = p.blendColor(m.col, getColorGray("spc", args, 1, p.color(0)), p.MULTIPLY);
        m.power = p.map( getFloat("power", args, 1, 0.0f), 0, 100, 0, 180 );
        String texName = getString("tex", args, 1, null);
        if(texName!=null) {
          m.tex = new MQOTex(texName);
        }
        materials[i] = m;
      }

      return cur;
    }

    // �I�u�W�F�N�g�̓ǂݍ���
    private int parseObjectChunk(String[] lines, int startLine) {
      int cur = startLine;
      cur = findChunkLine("Object", lines, cur);
      if(cur < 0) return -1;

      String[] args = splitArg(lines[cur++]);
      //p.println(args);
      if(args.length <= 1) return -1;
      
      MQOObject o = new MQOObject();
      o.name = args[1];
      
      int vertexLine = findChunkLine("vertex", lines, cur);
      if(vertexLine < 0) return -1;

      // �p�����[�^
      String s = "";
      for (int i=cur; i<vertexLine; i++) {
        s += " " + lines[cur++];
      }
      args = splitArg(s);
      //p.println(args);

      PVector zeroV = new PVector(0, 0, 0);
      PVector oneV = new PVector(1, 1, 1);
      o.depth = getByte("depth", args, 0, (byte)0);
      o.scale = getVector("scale", args, 0, oneV);
      PVector hpb = getVector("rotation", args, 0, zeroV);
      o.rotation = new PVector(hpb.y, hpb.x, hpb.z);
      o.translation = getVector("translation", args, 0, zeroV);
      o.visible = getInt("visible", args, 0, 15)!=0;
      o.shading = getByte("shading", args, 0, (byte)1);
      o.facet = getFloat("facet", args, 0, 0);
      o.col = getColor("int", args, 0, p.color(255));
      
      // ���_
      args = splitArg(lines[cur++]);
      if(args.length <= 1) return -1;
      int num = Integer.parseInt(args[1]);
      if(num<=0) return -1;
      
      o.vertices = new PVector[num];
      o.normals = new PVector[num];
      for(int i=0; i<num; i++) {
        args = splitArg(lines[cur++]);
        if(args.length<3) return -1;
        o.vertices[i] = new PVector(Float.parseFloat(args[0]), -Float.parseFloat(args[1]), Float.parseFloat(args[2]));
        o.normals[i] = new PVector(0, 0, 0);
      }

      // ��
      cur = findChunkLine("face", lines, cur);
      if(cur < 0) return -1;

      args = splitArg(lines[cur++]);
      if(args.length <= 1) return -1;
      num = Integer.parseInt(args[1]);
      if(num<=0) return -1;

      o.faces = new MQOFace[num];
      for(int i=0; i<num; i++) {
        args = splitArg(lines[cur++]);
        if(args.length<1) return -1;

        MQOFace face = new MQOFace();
        face.num = Byte.parseByte(args[0]);
        if(face.num<3 || face.num>4) return -1;

        face.index = new int[face.num];
        face.u = new float[face.num];
        face.v = new float[face.num];

        int c = findArg("V", face.num, args, 1);
        if(c<0) return -1;
        for(int j=0; j<face.num; j++) face.index[j] = Integer.parseInt(args[c+1+j]);

        face.materialIndex = getByte("M", args, 1, (byte)-1);

        c = findArg("UV", face.num*2, args, 1);
        if(c>=0) {
          for(int j=0; j<face.num; j++) {
            face.u[j] = Float.parseFloat(args[c+1+j*2]);
            face.v[j] = Float.parseFloat(args[c+2+j*2]);
          }
        }
        o.faces[i] = face;
      }

      objects.add(o);
      return cur;
    }

    // ���_�@��v�Z
    private void computeNormals() {
      if(objects==null) return;

      for (int i=0; i<objects.size(); i++) {
        MQOObject o = objects.get(i);
        if(!o.visible) continue;

        // ���_�@��v�Z
        for (int j=0; j<o.normals.length; j++) {
          o.normals[j].set(0, 0, 0);
        }
        for (int j=0; j<o.faces.length; j++) {
          MQOFace f = o.faces[j];
          PVector fn = o.getFaceNormal(j);
          o.normals[f.index[0]].add(fn);
          o.normals[f.index[1]].add(fn);
          o.normals[f.index[2]].add(fn);
          if(f.num==4) {
            //o.normals[f.index[0]].add(fn);
            //o.normals[f.index[2]].add(fn);
            o.normals[f.index[3]].add(fn);
          }
        }
        for (int j=0; j<o.normals.length; j++) {
          o.normals[j].normalize();
        }

        // �X���[�W���O�p�����Ē��_�@����C��
        for (int j=0; j<o.faces.length; j++) {
          MQOFace f = o.faces[j];
          PVector fn = o.getFaceNormal(j);
          for (int k=0; k<f.num; k++) {
            PVector vn = o.normals[f.index[k]];
            float facet = p.degrees( p.acos(PVector.dot(fn, vn)) );
            if(facet > o.facet) {
              o.normals[f.index[k]].set(fn); // �ʖ@����g���ăG�b�W���s��
            }
          }
        }
      }
    }

    // �ʂ��\�[�g
    private void sortFaces() {
      if(objects==null) return;

      // �}�e���A�����A�ʂ̒��_�����Ń\�[�g�p�̔�r���s���N���X
      class MQOFaceComparator implements Comparator<MQOFace> {
         public int compare(MQOFace f1, MQOFace f2){
           if(f1.materialIndex!=f2.materialIndex) {
             return f1.materialIndex < f2.materialIndex ? -1 : 1;
           } else {
             return f1.num < f2.num ? -1 : 1;
           }
         }
      }
      MQOFaceComparator faceComparator = new MQOFaceComparator();

      for (int i=0; i<objects.size(); i++) {
        MQOObject o = objects.get(i);
        Arrays.sort(o.faces, faceComparator);
      }
    }
    

}

