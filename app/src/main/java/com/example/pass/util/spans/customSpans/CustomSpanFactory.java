package com.example.pass.util.spans.customSpans;

public class CustomSpanFactory {
    public static CustomSpan getCustomSpanInstance(CustomSpan customSpan){
        if (customSpan instanceof MyForegroundColorSpan){
            return new MyForegroundColorSpan( ((MyForegroundColorSpan) customSpan).getForegroundColor());
        }else if (customSpan instanceof MyHighLightColoSpan){
            return new MyHighLightColoSpan(((MyHighLightColoSpan) customSpan).getBackgroundColor());
        }else if (customSpan instanceof MyNormalSpan){
            return new MyNormalSpan();
        }else if (customSpan instanceof MyShadeSpan){
            return new MyShadeSpan();
        }else if (customSpan instanceof MyStrikethroughSpan){
            return new MyStrikethroughSpan();
        }else if (customSpan instanceof MyStyleSpan){
            return new MyStyleSpan(((MyStyleSpan) customSpan).getStyle());
        }else if (customSpan instanceof MyUnderlineSpan){
            return new MyUnderlineSpan();
        }else{
            return null;
        }
    }
}
