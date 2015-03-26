<h1>Share to Mopidy</h1>
This android app lets you share content from the official apps to mopidy and to control basic playback functions (pause, play, next...)

<h2>Why I developed Share to Mopidy</h2>

<p>I was amazed how well <a href="https://www.spotify.com/es/connect/">Spotify Connect</a> works with my phone and computer.
I started to find something similar to control mopidy. </p>
<p>Unfortunately <a href="https://www.spotify.com/es/connect/">Spotify Connect</a> API is not currently public.
So I wanted to use something similar to control playback from the original Spotify app and I started working on this app using android share function.</p>
Note that this app is very simple and homemade.<br>
Bugs are expected.

<h2>How to use</h2>
<h3>Configure mopidy</h3>

The first thing is to set up mopidy correctly and install the mopidy extensions that you are going to use (<a href="https://github.com/mopidy/mopidy-spotify">Spotify</a>,<a href="https://github.com/dz0ny/mopidy-youtube">Youtube</a>).

Once mopidy is working connect the app to your mopidy server. The app has a tutorial to help you.
The connection is made using websocket so http must be enabled.

<h3>Spotify</h3>
You can share playlists, songs, artists... from official <a href="https://play.google.com/store/apps/details?id=com.spotify.music">Spotify android app</a> Simply tap share and send from email, share to mopidy should appear as an option.

<h3>Youtube</h3>
Share the video and share to mopidy will show up as an option.

<h2>Resources used</h2>
<ul>
<li><a href="https://github.com/koush/AndroidAsync">AndroidAsync</a> for websocket</li>
<li><a href="https://github.com/umano/AndroidSlidingUpPanel">Android Sliding Up Panel</a></li>
<li><a href="http://www.last.fm/api">Last FM API</a> to get album covers</li>
<li><a href="http://romannurik.github.io/AndroidAssetStudio/">Android Asset Studio</a> for icons
</ul>


