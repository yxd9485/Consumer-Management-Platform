<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String cpath = request.getContextPath();
    String imgCount = request.getParameter("imgCount");
    String imgSize = request.getParameter("imgSize");
%>
<script>
    $(function(){
    });
</script>

<style>
    .img-container{
        width: 130px; height: 120px;
    }
    .img-container section {
        width:100%; height:100%; position: relative;
    }
    .img-container .add-img {
        width: 100%; height: 100%; display: none;
    }
    .img-container .img-label{
        position: absolute; left: 0px; right: 0px; top: 65%; text-align: center;
    }
    .img-container .file{
        width: 100%; height: 100%; opacity: 0; position: absolute; top: 0px; left: 0px; z-index: 100;
    }

    .preview-section .add-img {
        display: block;
    }

    .up-section .close-upimg {
        position: absolute; top: 6px; right: 8px; display: none; z-index: 10;
    }
    .up-section .up-span {
        display: block; width: 100%; height: 100%; visibility: hidden; position: absolute; top: 0px; left: 0px; z-index: 200; background: rgba(0,0,0,.5);
    }
    .up-section:hover{
        border: 2px solid #f15134;
    }
    .up-section:hover .close-upimg{
        display: block;
    }
    .up-section:hover .up-span{
        visibility: visible;
    }
    }

</style>
<div class="img-container">
    <section class="add-section">

        <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
        <span class="img-label">50x50</span>
        <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>

        <span class="up-span"></span>
        <img class="close-upimg" src="<%=cpath %>/inc/vpoints/img/a7.png">
        <img class="preview-img" style="width:auto; margin:auto;" src="/springbootPlatform">
    </section>
</div>
