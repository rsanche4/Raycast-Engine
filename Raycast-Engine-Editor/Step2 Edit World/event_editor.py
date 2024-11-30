import tkinter as tk
from tkinter import filedialog
import os

sep = os.path.sep

def run_event_editor():

    def select_file():
        # Create a pop-up window
        root = tk.Tk()
        root.withdraw()  # Hide the root window

        # Ask user to select a file
        file_path = filedialog.askopenfilename(title="Which file in your current project folder is the script for this entity?")

        # Check if a file was selected
        if file_path:
            return file_path
        else:
            return None

    # Call the function and save the location in a variable
    selected_file = select_file()

    if selected_file:

        return selected_file.split("/")[-1]
    else:
        return None

    # if selected_file:
    #     print(f"Selected file location: {selected_file}")
    # else:
    #     print("No file selected.")



# import tkinter as tk
# import re

# def run_event_editor():
#     place_of_res = open(".."+sep+"project_location.txt", "r").read()
#     # Function to highlight Lua syntax
#     def highlight_syntax(text_widget):
#         text_widget.tag_remove("keyword", "1.0", "end")
#         text_widget.tag_remove("comment", "1.0", "end")
#         text_widget.tag_remove("string", "1.0", "end")

#         # Define Lua syntax elements (keywords, comments, strings)
#         # Define Lua syntax elements (keywords, comments, strings, numbers, and operators)
#         lua_keywords = r"\b(function|end|if|then|else|for|while|repeat|until|local|return|break|nil|true|false|do|in)\b"
#         lua_comments = r"--.*"  # Matches single-line comments starting with "--"
#         lua_strings = r"\".*?\"|\"[^\"]*\""  # Matches strings enclosed in double or single quotes

#         # Lua numbers: includes integers, floats, and scientific notation (e.g., 123, 3.14, 1e5)
#         lua_numbers = r"\b\d+(\.\d+)?([eE][+-]?\d+)?\b"

#         # Lua operators: includes assignment, comparison, arithmetic, logical operators, etc.
#         lua_operators = r"[=<>~%^&\|\+\-\*/%]=?|==|~=|\.\.|and|or|not"
                
#         # Highlight keywords
#         for match in re.finditer(lua_keywords, text_widget.get("1.0", "end")):
#             text_widget.tag_add("keyword", f"1.0 + {match.start()} chars", f"1.0 + {match.end()} chars")
        
#         # Highlight comments
#         for match in re.finditer(lua_comments, text_widget.get("1.0", "end")):
#             text_widget.tag_add("comment", f"1.0 + {match.start()} chars", f"1.0 + {match.end()} chars")
        
#         # Highlight strings
#         for match in re.finditer(lua_strings, text_widget.get("1.0", "end")):
#             text_widget.tag_add("string", f"1.0 + {match.start()} chars", f"1.0 + {match.end()} chars")

#         # Highlight numbers
#         for match in re.finditer(lua_numbers, text_widget.get("1.0", "end")):
#             text_widget.tag_add("number", f"1.0 + {match.start()} chars", f"1.0 + {match.end()} chars")
        
#         # Highlight operators
#         for match in re.finditer(lua_operators, text_widget.get("1.0", "end")):
#             text_widget.tag_add("operator", f"1.0 + {match.start()} chars", f"1.0 + {match.end()} chars")

            
#     # Function to save text as a Lua file
#     def save_file(text_widget):
#         global place_of_res
#         with open(place_of_res+"lua_code.lua", "w") as f:
#             f.write(text_widget.get("1.0", "end-1c"))  # Get all text and remove the extra newline at the end

#     # Initialize Tkinter window
#     root = tk.Tk()
#     root.title("Event Editor")

#     from screeninfo import get_monitors
#     w = get_monitors()[0].width//2
#     h = get_monitors()[0].height//2
#     posx = w
#     posy = h
#     root.geometry(f"{w}x{h}+{posx}+{posy}")


#     # Create a Text widget
#     text_widget = tk.Text(root, wrap="word", height=20, width=80)
#     text_widget.pack()

#     # Add a save button
#     save_button = tk.Button(root, text="Save", command=lambda: save_file(text_widget))
#     save_button.pack()

#     # Configure syntax highlighting tags
#     text_widget.tag_configure("keyword", foreground="blue", font=("Fira Code", 12, "bold"))
#     text_widget.tag_configure("comment", foreground="green", font=("Fira Code", 12, "italic"))
#     text_widget.tag_configure("string", foreground="red", font=("Fira Code", 12))
#     text_widget.tag_configure("number", foreground="lightblue", font=("Fira Code", 12))
#     text_widget.tag_configure("operator", foreground="orange", font=("Fira Code", 12))

#     # Bind the highlight function to key events (e.g., when typing)
#     text_widget.bind("<KeyRelease>", lambda event: highlight_syntax(text_widget))

#     # Start Tkinter event loop
#     root.mainloop()
