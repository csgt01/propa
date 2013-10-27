package models;

import java.util.List;

public interface IPlayGame {
   
   void setColours(List<Colour> colours);

   boolean placeAField(int row, int column, Colour colour);
   
   void setLeftPAnel(List<Row> rows);
   void setTopPanel(List<Column> columns);
   void wasRight(boolean isRight);
   void setupMatrix(int rows, int columns);
   
}
