package id.pras.xoxo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import id.pras.xoxo.logic.Evaluator;

public abstract class SimpleBoard extends Board{
  
  private Paint paint=new Paint();
  private int color;
  public SimpleBoard(Context context, int sideSize, int winSize) {
    super(context,sideSize,winSize);
    this.color=Color.WHITE;
  }
  
  public SimpleBoard(Context context, int sideSize, int winSize, byte player){
    super(context,sideSize,winSize,player);
    this.color=Color.WHITE;
  }
  
  public SimpleBoard(Context context, int sideSize, int winSize, byte player, int color){
    this(context,sideSize,winSize,player);
    setColor(color);
  }
  
  public void setColor(int color){
    this.color=color;
  }
  
  public void setColor(Color color){
    setColor(color.toArgb());
  }
  
  @Override
  protected void drawBoardLine(Canvas canvas, float x0, float y0, float x1, float y1) {
    canvas.drawLine(x0,y0,x1,y1,paint);
  }
  
  @Override
  protected void drawBackground(Canvas canvas) {
    // TODO: Implement this method
  }
  

  protected void drawX(Canvas canvas, int x, int y) {
    // Tentukan ukuran margin untuk memperkecil X
    float margin = getCellSize() / 5;

    // Hitung startX dan startY dengan menambahkan margin
    float startX = getOffsetX() + x * getCellSize() + margin;
    float startY = getOffsetY() + y * getCellSize() + margin;

    // Tentukan endX dan endY dengan mengurangi margin dari getCellSize()
    float endX = getOffsetX() + (x + 1) * getCellSize() - margin;
    float endY = getOffsetY() + (y + 1) * getCellSize() - margin;
    
    paint.setColor(Color.RED);
    paint.setStrokeWidth(8);

    // Gambar dua garis diagonal
    canvas.drawLine(startX, startY, endX, endY, paint);
    canvas.drawLine(endX, startY, startX, endY, paint);
  }

  protected void drawO(Canvas canvas, int x, int y) {
    float startX = getOffsetX() + x * getCellSize();
    float startY = getOffsetY() + y * getCellSize();
    paint.setColor(Color.BLUE);
    paint.setStrokeWidth(8);
    paint.setStyle(Paint.Style.STROKE);
    canvas.drawCircle(startX + getCellSize() / 2, startY + getCellSize() / 2, getCellSize() / 4f, paint);
  }
  
}
