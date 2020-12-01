//example script on the flappy bird game (collision detection not yet done)
//https://youtu.be/3QHknBwRqJg

__config() -> {'scope' -> 'global'};

inventory_set(player(),-1,1,'filled_map',{'map'->10102});
inventory_set(player(),0,1,'apple');

global_pipes = [];
global_bird = [64,0];
global_c = create_canvas();
global_gap = 40;
global_speed = 1;
global_gravity = 0.7;

global_started = false;

__on_tick() -> (
    if(global_started,
      if(tick_time()%(50/global_speed) == 0,
          global_pipes += [128,rand(120-global_gap-10)+10];
      );
      for(global_pipes,
          global_pipes:_i:0 = (_:0-global_speed);

      );
      global_pipes = filter(global_pipes,!(_:0 < -15));
      global_bird:1 += global_gravity;
      global_bird:0 += global_bird:1;
    );
    __draw();
    draw_map(global_c,10102);
);

__draw() -> (
  fill_canvas(global_c,rgb_to_map(0,255,255));
  rectangle(global_c,rgb_to_map(0,160,0),0,120,128,8);
  for(global_pipes,
    __draw_pipe(_:0,_:1);
  );
  ellipse(global_c,rgb_to_map(255,0,0),30,global_bird:0,10,10);
);


__draw_pipe(x,y) -> (
    rectangle(global_c,rgb_to_map(255,255,0),x,0,15,y);
    rectangle(global_c,rgb_to_map(255,255,0),x,y+global_gap,15,128-y-global_gap-8+1);
);

__on_player_drops_item(player) -> (
  schedule(0,'__restock',player);
  global_bird:1 = -4;
  global_started = true;
);

__restock(player) -> (
    inventory_set(player,0,1,'apple');
    run('kill @e[type=item]');
)
