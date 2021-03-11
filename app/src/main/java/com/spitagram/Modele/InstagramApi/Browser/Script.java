package com.spitagram.Modele.InstagramApi.Browser;

public class Script {

    public static final String EXTRACT_TEXT = "function getAllText(){ " +
            "return (document.getElementsByTagName('pre')[0].innerHTML); };";
    public static final String FUNCTION_EXTRACT_TEXT = "getAllText()";

    public static final String RECUP_HTML = "<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>";

    public static final String UNFOLLOW = "document.getElementsByClassName('_5f5mN    -fzfL     _6VtSN     yZn4P   ')[0].click(); " +
            "document.getElementsByClassName('aOOlW -Cab_    ')[0].click();";

    public static final String FOLLOW = "document.getElementsByClassName('_5f5mN       jIbKX  _6VtSN     yZn4P   ')[0].click();";
}
