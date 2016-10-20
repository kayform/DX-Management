<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<header id="menu">
    <div class="app-bar fixed-top darcula" data-role="appbar">
        <a class="app-bar-element branding">BrandName</a>
        <span class="app-bar-divider"></span>
        <ul class="app-bar-menu">
            <li><a href="">Dashboard</a></li>
            <li>
                <a href="" class="dropdown-toggle">Project</a>
                <ul class="d-menu" data-role="dropdown">
                    <li><a href="">New project</a></li>
                    <li class="divider"></li>
                    <li>
                        <a href="" class="dropdown-toggle">Reopen</a>
                        <ul class="d-menu" data-role="dropdown">
                            <li><a href="">Project 1</a></li>
                            <li><a href="">Project 2</a></li>
                            <li><a href="">Project 3</a></li>
                            <li class="divider"></li>
                            <li><a href="">Clear list</a></li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li><a href="">Security</a></li>
            <li><a href="">System</a></li>
            <li>
                <a href="" class="dropdown-toggle">Help</a>
                <ul class="d-menu" data-role="dropdown">
                    <li><a href="">ChatOn</a></li>
                    <li><a href="">Community support</a></li>
                    <li class="divider"></li>
                    <li><a href="">About</a></li>
                </ul>
            </li>
        </ul>
        <div class="app-bar-element place-right">
            <span class="dropdown-toggle"><span class="mif-cog"></span> User Name</span>
            <div class="app-bar-drop-container padding10 place-right no-margin-top block-shadow fg-dark" data-role="dropdown" data-no-close="true" style="width: 220px">
                <h2 class="text-light">Quick settings</h2>
                <ul class="unstyled-list fg-dark">
                    <li><a href="" class="fg-white1 fg-hover-yellow">Profile</a></li>
                    <li><a href="" class="fg-white2 fg-hover-yellow">Security</a></li>
                    <li><a href="" class="fg-white3 fg-hover-yellow">Exit</a></li>
                </ul>
            </div>
        </div>
    </div>
</header>