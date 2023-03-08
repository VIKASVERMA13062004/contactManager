console.log("vikas")
var a =document.getElementById('password')
function f()
{
	var x = a.attributes["type"]
	 
	 
		a.setAttribute("type", "text");
}
function g()
{
	var x = a.attributes["type"]
	 
	 
		a.setAttribute("type", "password");
}

const toogleSlidebar=()=>{
	if($(".slidebar").is(":visible"))
	{
		$(".slidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
	}
	else{
		$(".slidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
}


$("#vv").click(function()
{
	 var s=$("#firstname").val();
	 console.log(s);
	 if(s==null)
	 {
		alert("fill entery");
	}
});