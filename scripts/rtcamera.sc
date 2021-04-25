// rtcamera by MediumDifficulty

__command()->
(
    'Very informative help menu'
);

global_settings =
{
    ['map', 28839]
    ['fov', 90],
    ['cpos', [0, 0, 0]],
    ['ray_length', 20],
    ['ray_steps', 16]
};

global_is_task = false;
renderTask() ->
(
    task('renderNormal');
    print(format('l STARTED RENDER!'));
    null
);

giveMap() ->
(
    run('give @s filled_map{map:28839} 1');
    null
);

renderNormal() ->
(
    // Input parameters
    fov = global_settings:'fov';
    screen_width = 128;
    screen_height = 128;
    cpos = global_settings:'cpos';
    player = player();
    yaw = player ~ 'yaw';
    pitch = player ~ 'pitch';
    cpos = player ~ 'pos' + [0, player ~ 'eye_height', 0];

    scale = tan(fov * 0.5);
    aspect_ratio = screen_width / screen_height;

    sinp = sin(pitch);
    cosp = cos(pitch);
    siny = sin(yaw);
    cosy = cos(yaw);

    u = [    cosy,      0,    siny   ];
    v = [-siny*sinp,  cosp, cosy*sinp];
    w = [-siny*cosp, -sinp, cosy*cosp];
    q = (screen_width/2)*u + (screen_height/2)*v + (screen_height/(2*scale))*w;

    for(range(screen_width),
        sx = _ + 0.5;
        for(range(screen_height),
            sy = _ + 0.5;

            cdir = -sx*u -sy*v + q;

            [b, isShadow] = __cast_ray(cpos, cdir);
            color = block_color(b, if(isShadow, 3, 1));
            set_pixel(global_canvas, sx, sy, color);
        )
    );
    draw_map(global_canvas, global_settings:'map');
    if(global_is_task,
        global_is_task = false;
        print(format('l DONE!'));
    )
    null
);

__cast_ray(ray_start, ray_direction) ->
(
    block_limit = global_settings:'ray_length';
    steps_per_block = global_settings:'ray_steps';

    max_steps = block_limit * steps_per_block;

    len = sqrt(ray_direction:0 ^ 2 + ray_direction:1 ^ 2 + ray_direction:2 ^ 2);
    ray_step = ray_direction / len / steps_per_block;
    
    current_ray_pos = ray_start;
    while(air(current_ray_pos), max_steps,
        current_ray_pos = current_ray_pos + ray_step;
    );
    b = block(current_ray_pos);
    
    if(block(current_ray_pos) != 'air',
        current_ray_pos = current_ray_pos + [0, 1 / steps_per_block, 0];
        while(air(current_ray_pos), max_steps,
            current_ray_pos = current_ray_pos + [0, 1 / steps_per_block, 0];
       );
    );
    isShadow = bool(!air(current_ray_pos));
    [b, isShadow]
);

// Settings
setFov(int) ->
(
    global_settings:'fov' = int;
    print(format('l Set FOV to ', 'e ' + int));
    null
);

setCameraPosition(x, y, z) ->
(
    global_settings:'cpos' = [x, y, z];
    print(format('l Set CAMERAPOSITION to ', 'e x:' + x + ' ', 'e y:' + y + ' ', 'e z:' + z));
    null
);

setRayLength(int) ->
(
    global_settings:'ray_length' = int;
    print(format('l Set RAYLENGTH to ', 'e ' + int));
    null
);

setRaySteps(int) ->
(
    global_settings:'ray_steps' = int;
    print(format('l Set RAYSTEPS to ', 'e ' + int));
    null
);

global_canvas = create_canvas();
