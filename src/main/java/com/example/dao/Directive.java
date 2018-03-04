package com.example.dao;

/**
 * Created by damei on 18/1/16.
 */
public class Directive {
    DirectiveItems[] directive_items; //开发者需要音箱设备播报的内容 注：音箱会依据开发者给出的顺序播报

    public DirectiveItems[] getDirective_items() {
        return directive_items;
    }

    public void setDirective_items(DirectiveItems[] directive_items) {
        this.directive_items = directive_items;
    }
}
