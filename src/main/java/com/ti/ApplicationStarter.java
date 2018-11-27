package com.ti;

import com.ti.dao.ItemDao;
import com.ti.dao.impl.ItemDaoImpl;
import com.ti.entities.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class ApplicationStarter {
    private static void printMenu() {
        System.out.println(" -------数据模拟操作------");
        System.out.println("|1.查询所有数据            |");
        System.out.println("|2.根据条件查询数据        |");
        System.out.println("|3.根据条件添加数据        |");
        System.out.println("|4.根据条件跟新数据        |");
        System.out.println("|5.根据条件删除数据        |");
        System.out.println("|6.退出模拟操作            |");
        System.out.println(" ------------------------");
    }

    public static void main(String[] args) {
        ItemDao itemDao = new ItemDaoImpl();
        Scanner in = new Scanner(System.in);
        out:
        while (true) {
            printMenu();
            String s = in.nextLine();
            if (s == null) {
                System.out.println("输入有误请重新输入");
                continue;
            }
            int selection = 0;
            try {
                selection = Integer.parseInt(s);
            } catch (Exception e) {
                System.out.println("请输入数字");
                continue;
            }
            switch (selection) {
                case 1: {
                    ArrayList<Item> allItem = itemDao.findAllItem();
                    allItem.forEach(o -> {
                        System.out.println(o);
                    });
                    break;
                }
                case 2: {
                    Item item = new Item();
                    setItemValue(in, item);
                    ArrayList<Item> items = itemDao.findIem(item);
                    items.forEach(o->{
                        System.out.println(o);
                    });
                    break;
                }
                case 3: {
                    Item item = new Item();
                    setItemValue(in, item);
                    itemDao.addItem(item);
                    break;
                }
                case 4: {
                    Item item = new Item();
                    setItemValue(in, item);
                    itemDao.updateItem(item);
                    break;
                }
                case 5: {
                    Item item = new Item();
                    setItemValue(in, item);
                    itemDao.deleteItem(item);
                    break;
                }
                case 6: {
                    break out;
                }
            }
        }
    }

    private static void setItemValue(Scanner in, Item item) {
        System.out.println("请输入id");
        String id = in.nextLine();

        try {
            int idd = Integer.parseInt(id);
            item.setId(idd);
        } catch (Exception e) {

        }

        System.out.println("请输入name");
        String name = in.nextLine();
        if (!name.isEmpty()) {
            item.setName(name);
        }

        System.out.println("请输入count");
        String count = in.nextLine();
        try {
            int c = Integer.parseInt(count);
            item.setCount(c);
        } catch (Exception e) {

        }
        System.out.println("请输入price");
        String price = in.nextLine();
        try {
            double p = Double.parseDouble(price);
            item.setPrice(p);
        } catch (Exception e) {

        }
    }

}
