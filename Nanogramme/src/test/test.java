package test;

import java.util.ArrayList;

import java.util.Date;

import java.util.LinkedList;

public class test {

   public static void main(String[] args) {

      new test().start();

   }

   class Block {

      int start;

      int end;

      int min;

      int max;

   }

   private void start() {

      LinkedList<Block> blocks = new LinkedList<Block>();

      Block block3 = new Block();

      Block block2 = new Block();

      Block block1 = new Block();

      block1.start = 0;

      block1.end = 0;

      block1.min = 0;

      block1.max = 25;

      block2.start = 2;

      block2.end = 2;

      block2.min = 2;

      block2.max = 27;

      block3.start = 4;

      block3.end = 5;

      block3.min = 4;

      block3.max = 29;

      blocks.add(block1);

      blocks.add(block2);

      blocks.add(block3);

      ArrayList<LinkedList<String>> possibilities = new ArrayList<LinkedList<String>>();

      LinkedList<String> aa = new LinkedList<String>();

      aa.add("A");

      aa.add("-");

      aa.add("A");

      aa.add("-");

      aa.add("A");

      aa.add("A");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      aa.add("-");

      System.out.println(aa + "start");

      // possibilities = statisch(block1, block2, block3, possibilities, aa);

      Date date = new Date();

      possibilities = rekursiv(blocks, possibilities, aa, 0, 0);

      System.out.println("-------------");

      System.out.println("took " + (new Date().getTime() - date.getTime()) + " ms");

      System.out.println(possibilities.size());

   }

   private ArrayList<LinkedList<String>> rekursiv(LinkedList<Block> blocks1, ArrayList<LinkedList<String>> possibilities,

   LinkedList<String> aa, int numberOfBlock, int add1) {

      int add = add1;

      LinkedList<Block> blocks = new LinkedList<Block>(blocks1);

      ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(

      possibilities);

      blocks.get(numberOfBlock).start = blocks.get(numberOfBlock).min + add;

      int numberOfBlock2 = numberOfBlock + 1;

      for (int i = blocks.get(numberOfBlock).start; i <= blocks.get(numberOfBlock).max; i++) {

         LinkedList<String> bb = new LinkedList<String>(aa);

         if (numberOfBlock2 < blocks.size()) {

            possibilities2 = rekursiv(blocks, possibilities2, bb, numberOfBlock2, add);

         }

         if (aa.getLast() == "-") {

            aa.removeLast();

            aa.add(blocks.get(numberOfBlock).start, "-");

            possibilities2.add(aa);
            System.out.println(possibilities2);

            System.out.println(aa + "i:" + add);

         }

         blocks.get(numberOfBlock).start++;

         add++;

      }

      return possibilities2;

   }

//   private ArrayList<LinkedList<String>> statisch(Block block1, Block block2,
//
//   Block block3, ArrayList<LinkedList<String>> possibilities,
//
//   LinkedList<String> aa) {
//
//      int add = 0;
//
//      ArrayList<LinkedList<String>> possibilities2 = new ArrayList<LinkedList<String>>(
//
//      possibilities);
//
//      for (int i = block1.start; i <= block1.max; i++) {
//
//         LinkedList<String> bb = new LinkedList<String>(aa);
//
//         block2.start = block2.min + add;
//
//         for (int j = block2.end + add; j < block2.max; j++) {
//
//            LinkedList<String> cc = new LinkedList<String>(bb);
//
//            block3.start = block3.min + add;
//
//            for (int k = block3.end + add; k < block3.max; k++) {
//
//               if (cc.getLast() == "-") {
//
//                  cc.removeLast();
//
//                  cc.add(block3.start, "-");
//
//                  possibilities2.add(cc);
//
//                  System.out.println(cc + "k:" + add);
//
//               }
//
//            }
//
//            if (bb.getLast() == "-") {
//
//               bb.removeLast();
//
//               bb.add(block2.start, "-");
//
//               possibilities2.add(bb);
//
//               System.out.println(bb + "j:" + add);
//
//            }
//
//         }
//
//         if (aa.getLast() == "-") {
//
//            aa.removeLast();
//
//            aa.add(block1.start, "-");
//
//            possibilities2.add(aa);
//
//            System.out.println(aa + "i:" + add);
//
//         }
//
//         block1.start++;
//
//         add++;
//
//      }
//
//      return possibilities2;
//
//   }

}