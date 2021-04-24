# How it works

All maps with an id of above 10.000 will work with canvases (since those don't get updates from the server with this mod, exept if you use the draw_map function)


# Functions

### `create_canvas()`

Returns a new canvas value

### `get_pixel(canvas, x, y)`

Read the color of a specified pixel. Returns it as a map color id.

### `set_pixel(canvas, x, y, color)`

Set a specified pixel to a color. Color as map color id.

### `fill_canvas(canvas, color)`

Sets all pixels of a canvas to a color.

### `rectangle(canvas, color, x, y, width, height)`

Draws a rectangle on x and y position (top right corner) with a specified width and height

### `ellipse(canvas, color, x, y, width, height)`

Draws an ellipse with center at x and y position, with specified width and height.

### `line(canvas, color, x1, y1, x2, y2)`

Draws a line from pos x1,y1 to x2,y2

### `draw_map(canvas, id)`

Sends the content of the canvas to all maps with the specied id

### `rgb_to_map(r, g, b)`

Returns the map color id that is the closest matching the rgb values
