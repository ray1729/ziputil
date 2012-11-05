# ziputil

A simple Clojure wrapper anound java.util.zip for creating zip archives.

## Installation

Available from Clojars. Using Leiningen, include the following
dependency in `project.clj`:

    [ziputil "0.1.0"]

## Usage

    (use '[ziputil.core :only [create-archive] :as ziputil])

    (create-archive "foo.zip" "file1.txt" file1 "file2.txt" file2)

Passing a :prefix option will add that prefix to the filenames in the archive:

    (create-archive "foo.zip" {:prefix "some/dir/"} "file1.txt" file1 "file2.txt" file2)

is equivalent to:

    (create-archive "foo.zip" "some/dir/file1.txt" file1 "some/dir/file2.txt" file2)

The compression level may also be specified in the options:

    (create-archive "foo.zip" {:compression-level ziputil/best-compression} "file1.txt" file1 "file2.txt" file2)

## See Also

fs - file system utilities for Clojure - provides similar
functionality in fs.compression, but with a different API:
<https://github.com/Raynes/fs>.

## License

Copyright © 2012 Ray Miller <ray@1729.org.uk>

Distributed under the Eclipse Public License, the same as Clojure.
