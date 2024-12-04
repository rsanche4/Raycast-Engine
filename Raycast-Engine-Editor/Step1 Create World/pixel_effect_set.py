import tkinter as tk
from tkinter import Label
from PIL import Image, ImageTk
import os
import json



# List of allowed values for the slider
allowed_values = [2, 4, 5, 10]

# Function to update the label and image based on the slider value
def update_effect(val):
    effect_value = int(val)
    label_effect.config(text=f"Pixel Effect: {effect_value}")

    sep =  os.path.sep

    projects_path = open(".."+sep+"project_location.txt", "r").read()

    current_selected_world = current_selected_world = open(".."+sep+".tempdata.txt", "r").read()

    worlds_data_path = projects_path+ ""+sep+"worlds_data.json"
    with open(worlds_data_path, "r") as file:
        data = json.load(file)

    index_to_update = -1
    for j, allvars in enumerate(data["world_data"]):
        if allvars["VAR0"].split(":")[1]==current_selected_world:
            
            index_to_update=j
            break
                    
    if index_to_update!=-1:
        # we selected the world. Now let"s check if that world has a map cuz this is where we stored the map info
        data["world_data"][index_to_update]["VAR10"] = f"pixel_effect:{effect_value}"

        with open(worlds_data_path, "w") as file:
            json.dump(data, file, indent=4)  # The indent argument adds pretty formatting


    
    # Load and update the image based on the effect value
    img_path = f"pixel_effect_{effect_value}.png"  # You should have images like pixel_effect_2.png, pixel_effect_4.png, etc.
    try:
        img = Image.open(img_path)
        img = img.resize((400, 400))  # Resize the image to fit the label size
        img_tk = ImageTk.PhotoImage(img)
        label_image.config(image=img_tk)
        label_image.image = img_tk  # Keep a reference to the image to prevent it from being garbage collected
    except FileNotFoundError:
        label_image.config(image='')  # Clear the image if not found

# Function to adjust the slider's value to one of the allowed values
def set_slider_value(val):
    if int(val) not in allowed_values:
        # Find the closest allowed value
        closest_value = min(allowed_values, key=lambda x: abs(x - int(val)))
        slider.set(closest_value)
        update_effect(closest_value)
    else:
        update_effect(val)

# Set up the main window
root = tk.Tk()
root.title("Pixel Effect Slider")

# Create a slider with the specified range and resolution, but adjust its value based on allowed values
slider = tk.Scale(root, from_=2, to=max(allowed_values), orient="horizontal", tickinterval=10, resolution=1, showvalue=False, command=set_slider_value)
slider.set(2)  # Set default value to 2
slider.pack(pady=20)

# Label to show the selected pixel effect value
label_effect = Label(root, text="Pixel Effect: 2", font=("Arial", 14))
label_effect.pack()

# Label to display the image based on the selected value
label_image = Label(root)
label_image.pack(pady=20)

# Start the Tkinter event loop
root.mainloop()
