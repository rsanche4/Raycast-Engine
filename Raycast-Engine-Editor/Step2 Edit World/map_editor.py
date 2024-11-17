import tkinter as tk
import random
import os
from PIL import Image, ImageTk

# Create the main window
root = tk.Tk()

from screeninfo import get_monitors
root.title("Tilemap")
w = get_monitors()[0].width//2
h = get_monitors()[0].height
posx = w
posy = 0
root.geometry(f"{w}x{h}+{posx}+{posy}")
#root.resizable(False, False)

# Define the directory path
place_of_res = open('..\\project_location.txt', 'r').read()

# Get all files in the directory
files = os.listdir(place_of_res)

layer_options = ["Blocks", "Sprites"]

# Filter out only the block#.png files
image_files = [f for f in files if f.startswith("block") and f.endswith(".png")]

# Load the images
images = []
for image_file in image_files:
    image_path = os.path.join(place_of_res, image_file)
    images.append(tk.PhotoImage(file=image_path))

current_image = images[0]

# Function to generate and display a random number
def showimgselected(i, index):
    global current_image
    current_image=i
    blockchosen.config(image=current_image)

    with open("selected_texture.txt", "w") as tempfile:
        tempfile.write(image_files[index])

# Create a canvas and a scrollbar
canvas = tk.Canvas(root)
scrollbar = tk.Scrollbar(root, orient="vertical", command=canvas.yview)
canvas.configure(yscrollcommand=scrollbar.set)

# Create a frame inside the canvas to hold all the buttons
button_frame = tk.Frame(canvas)

# Place the button frame on the canvas
canvas.create_window((0, 0), window=button_frame, anchor="nw")

# Define the grid dimensions (number of columns in each row)
num_columns = 8  # Adjust this number to change the grid's width

# Add buttons for each image inside the button frame in a grid layout
for index, img in enumerate(images):
    row = index // num_columns
    col = index % num_columns
    button = tk.Button(button_frame, image=img, command= lambda i=img, ind=index: showimgselected(i, ind))
    button.grid(row=row, column=col, padx=10, pady=10)


def update_selected_layer(layer):
    global place_of_res
    global canvas
    global button_frame
    canvas.delete("all")
    button_frame.destroy()
    canvas.configure(yscrollcommand=scrollbar.set)
    button_frame = tk.Frame(canvas)
    canvas.create_window((0, 0), window=button_frame, anchor="nw")
    global image_files
    if layer=="Sprites":
        image_files =[f for f in files if f.startswith("sprite") and f.endswith(".png")]
    elif layer=="Blocks":
        image_files=[f for f in files if f.startswith("block") and f.endswith(".png")]
    global images
    images = []
    for image_file in image_files:
        image_path = os.path.join(place_of_res, image_file)
        
        # Open the image
        img = Image.open(image_path)  # Replace with your image path
        
        # Define the crop box (left, upper, right, lower)
        crop_box = (0, 0, 64, 64)

        # Crop the image
        cropped_img = img.crop(crop_box)
        
        converting_to_tk = ImageTk.PhotoImage(cropped_img)

        images.append(converting_to_tk )

    # Add buttons for each image inside the button frame in a grid layout
    for index, img in enumerate(images):
        row = index // num_columns
        col = index % num_columns
        button = tk.Button(button_frame, image=img, command= lambda i=img, ind=index: showimgselected(i,ind))
        button.grid(row=row, column=col, padx=10, pady=10)
    
    


# Update the scrollable region
button_frame.update_idletasks()  # Update the frame size
canvas.config(scrollregion=canvas.bbox("all"))

# The showing of which block we have chosen
blockchosen = tk.Label(root, image=current_image,bd=2, relief="solid")
blockchosen.pack()
# Add the ability to select which layer we will be placing
dropdown = tk.OptionMenu(root, tk.StringVar(root, layer_options[0]), *layer_options, command=update_selected_layer)
dropdown.pack()
# Pack the canvas and scrollbar
canvas.pack(side="left", fill="both", expand=True)
scrollbar.pack(side="right", fill="y")


root.mainloop()
