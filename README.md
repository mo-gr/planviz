planviz
=======


The easiest way to get this up and running for tinkering with it is to install [leiningen](http://leiningen.org).

Then simply run `lein figwheel dev` in the project root.

This should download all the dependencies, compile the ClojureScript and start a development webserver on port 3449.

If there are no errors, simply open a `http://localhost:3449/index-dev.html` in your browser of choice.

Figwheel automatically watches the code (most of which lives in `src/cljs/devplatform/core.cljs`) for changes, does the recompilation and even updates the browser.