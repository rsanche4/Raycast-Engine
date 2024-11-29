import tkinter as tk
from tkinter import ttk, colorchooser
import colorsys
from screeninfo import get_monitors
from colorsys import rgb_to_hls
import json

projects_path = open('..\\project_location.txt', 'r').read()

def delete_world(entries):
    global projects_path
    if entries["world_name"].get()=="":
        return
    
    worlds_data_path = projects_path+ "\\worlds_data.json"

    with open(worlds_data_path, 'r') as file:
        data = json.load(file)  # Parse the JSON file into a Python dictionary 

    # Check if it's there is anything in there, if not insert. otherwise already there by that name edit it, otherwise then push it.
    index_to_update = -1
    for j, allvars in enumerate(data["world_data"]):
        if allvars["VAR0"].split(":")[1]==entries["world_name"].get():
            
            index_to_update=j
            break
    
    if index_to_update!=-1:
        data["world_data"].pop(index_to_update)
    
        # Open a file in write mode ('w') and dump JSON data into it
        with open(worlds_data_path, 'w') as file:
            json.dump(data, file, indent=4)  # The indent argument adds pretty formatting

def get_world_names():
    global projects_path
    worlds_data_path = projects_path+ "\\worlds_data.json"

    with open(worlds_data_path, 'r') as file:
        data = json.load(file)  # Parse the JSON file into a Python dictionary


    if len(data["world_data"])==0:
        return []
    else:
        worldnames = []
        for vars in data["world_data"]:
            worldnames.append(vars["VAR0"].split(":")[1])

        return worldnames


def upsert(entries, fog_color):
    global projects_path
    if entries["world_name"].get()=="":
        return
    
    worlds_data_path = projects_path+ "\\worlds_data.json"

    with open(worlds_data_path, 'r') as file:
        data = json.load(file)  # Parse the JSON file into a Python dictionary 

    # Check if it's there is anything in there, if not insert. otherwise already there by that name edit it, otherwise then push it.
    index_to_update = -1
    for j, allvars in enumerate(data["world_data"]):
        if allvars["VAR0"].split(":")[1]==entries["world_name"].get():
            
            index_to_update=j
            break
    
    if index_to_update==-1:
        
        data["world_data"].append({f"VAR{i}": "" for i in range(1000)})

        keys = list(entries.keys())
        
        for i in range(len(keys)):
            
            data["world_data"][len(data["world_data"])-1][f"VAR{i}"] = f"{keys[i]}:{entries[keys[i]].get()}"

        # Open a file in write mode ('w') and dump JSON data into it
        with open(worlds_data_path, 'w') as file:
            json.dump(data, file, indent=4)  # The indent argument adds pretty formatting

    else:
        
        keys = list(entries.keys())
        
        for i in range(len(keys)):
            
            data["world_data"][index_to_update][f"VAR{i}"] = f"{keys[i]}:{entries[keys[i]].get()}"

        data["world_data"][index_to_update][f"VAR{len(keys)}"] = f"fog_color:{fog_color}"
        
        # Open a file in write mode ('w') and dump JSON data into it
        with open(worlds_data_path, 'w') as file:
            json.dump(data, file, indent=4)  # The indent argument adds pretty formatting

    
    # All went smooth and we updated it, which means now save it in temp so the rest of UI knows which world we are updating
    with open("..\\.tempdata.txt", "w") as tempfile:
        tempfile.write(entries["world_name"].get())



def sort_colors_by_spectrum(rgb_list):
    def hex_to_rgb(hex_color):
        hex_color = hex_color.lstrip('#')  
        return tuple(int(hex_color[i:i + 2], 16) for i in (0, 2, 4))

    def rgb_to_hsl(rgb):
        r, g, b = [x / 255.0 for x in rgb]
        h, l, s = rgb_to_hls(r, g, b)
        return h, s, l

    sorted_colors = sorted(list(map(hex_to_rgb, rgb_list)), key=lambda rgb: rgb_to_hsl(rgb))
    return list(map(lambda rgb: "#{:02X}{:02X}{:02X}".format(*rgb), sorted_colors))

def generate_colors(n):
    colors = []
    r = range(256)
    g = range(256)
    b = range(256)
    for red in r:
        for green in g:
            for blue in b:
                colors.append((red, green, blue))

    total_number_of_colors = len(colors)
    steps = total_number_of_colors // n
    colors_return = []
    for i in range(0, total_number_of_colors, steps):
        colors_return.append(f"#{colors[i][0]:02x}{colors[i][1]:02x}{colors[i][2]:02x}")
    colors_return.pop(0)

    return sort_colors_by_spectrum(colors_return)


# Create the main window
root = tk.Tk()
root.title("World Settings")
#root.configure(background="#000000")
w = get_monitors()[0].width // 2
h = get_monitors()[0].height // 2 +150
posx = (get_monitors()[0].width-w)//2
posy = (get_monitors()[0].height-h)//2
root.geometry(f"{w}x{h}+{posx}+{posy}")
# root.resizable(False, False)

# Create a Canvas widget
canvas = tk.Canvas(root)
canvas.pack(side="left", fill="both", expand=True)

# Create a vertical Scrollbar and link it to the canvas
scrollbar = tk.Scrollbar(root, orient="vertical", command=canvas.yview)
scrollbar.pack(side="right", fill="y")

# Configure the canvas to use the scrollbar
canvas.configure(yscrollcommand=scrollbar.set)

# Create a frame inside the canvas to hold all the content
content_frame = tk.Frame(canvas)
canvas.create_window((0, 0), window=content_frame, anchor="nw")

# Dictionary to hold entry widgets or dropdowns for each variable
entries = {}

variables = {
    "world_name": str,
    "render_distance": int,
    "sky_self_movement": bool,
    "walking_speed": float,
    "turning_speed": float
}

# Loop to create widgets for each variable
for row, (var_name, var_type) in enumerate(variables.items()):
    # Label for each variable
    label = tk.Label(content_frame, text=var_name.replace("_", " ").title())
    label.grid(row=row, column=0, padx=10, pady=5, sticky="w")

    # Handle different variable types
    if var_name == "world_name":
        entry = ttk.Combobox(content_frame, values=get_world_names(), state="normal")
        entry.grid(row=row, column=1, padx=10, pady=5)
        entries[var_name] = entry
    elif var_type == str:
        entry = tk.Entry(content_frame)
        entry.grid(row=row, column=1, padx=10, pady=5)
        entries[var_name] = entry
    elif var_type == float:
        entry = tk.Entry(content_frame)
        entry.grid(row=row, column=1, padx=10, pady=5)
        entries[var_name] = entry
    elif var_type == int:
        entry = tk.Entry(content_frame)
        entry.grid(row=row, column=1, padx=10, pady=5)
        entries[var_name] = entry
    elif var_type == bool:
        var = tk.StringVar(value="false")
        dropdown = ttk.Combobox(content_frame, textvariable=var, values=["true", "false"], state="readonly")
        dropdown.grid(row=row, column=1, padx=10, pady=5)
        entries[var_name] = dropdown


# Variable to hold the selected fog color
fog_color_var = tk.StringVar(value="#000000")  # Default color is black

# Fog Color Entry
label = tk.Label(content_frame, text="Fog Color:")
label.grid(row=row + 1, column=0, padx=10, pady=5, sticky="w")
entry = tk.Entry(content_frame)
entry.grid(row=row + 1, column=1, padx=10, pady=5)

# Create a frame to hold the color buttons
color_frame = tk.Frame(content_frame)
color_frame.grid(row=row + 2, column=0, columnspan=2, padx=10, pady=5)

# Generate 200 color buttons
colors = generate_colors(200)

# Loop through the colors and create buttons
for i, color in enumerate(colors):
    color_button = tk.Button(color_frame, bg=color, width=2, height=1,
                             command=lambda color=color: choose_color(color))
    color_button.grid(row=i // 20, column=i % 20, padx=5, pady=5)  # 10 buttons per row

# Function to set the selected color in fog_color_var
def choose_color(color):
    fog_color_var.set(color)
    entry.config(textvariable=fog_color_var)

# Function to handle the submit button click
def submit():
    # Print the values
    upsert(entries, fog_color_var.get())

def delete():
    delete_world(entries)

# Create a submit button
submit_button = tk.Button(content_frame, text="Upsert", command=submit)
submit_button.grid(row=row + 3, column=0, columnspan=2, padx=10, pady=5)

delete_button = tk.Button(content_frame, text="Delete", command=delete)
delete_button.grid(row=row + 4, column=0, columnspan=2, padx=10, pady=5)

# Update the scroll region of the canvas when the content changes
content_frame.update_idletasks()
canvas.config(scrollregion=canvas.bbox("all"))

# Start the Tkinter main loop
root.mainloop()
