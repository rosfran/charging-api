import {
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import { useSnackbar } from "notistack";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";
import Sidebar from "../../components/sidebar/Sidebar";
import HttpService from "../../services/HttpService";
import AuthService from "../../services/AuthService";
import "./solargrid.scss";

const EditSolarGrid = () => {
  const pageTitle = "Edit Solar Grid";
  const { state } = useLocation();
  const defaultValues = {
    id: state.id,
    name: state.name,
    typeId: state.solarGrid.id,
    userId: state.user.id,
  };
  const { enqueueSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const [formValues, setFormValues] = useState(defaultValues);
  const [solarGrids, setSolarGrids] = useState([]);

  useEffect(() => {
    const getSolarGrids = async () => {
      const userId = AuthService.getCurrentUser()?.id;
      const response = await HttpService.getWithAuth("/solar-grid/users/"+userId);
      const solarGrids = await response.data.content;
      setSolarGrids(solarGrids);
    };
    getSolarGrids();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    HttpService.putWithAuth("/api/v1/solar-grid", formValues)
      .then((response) => {
        enqueueSnackbar("Solar-grid updated successfully", { variant: "success" });
        navigate("/solargrid");
      })
      .catch((error) => {
        if (error.response?.data?.errors) {
          error.response?.data?.errors.map((e) =>
            enqueueSnackbar(e.field + " " + e.message, { variant: "error" })
          );
        } else if (error.response?.data?.message) {
          enqueueSnackbar(error.response?.data?.message, { variant: "error" });
        } else {
          enqueueSnackbar(error.message, { variant: "error" });
        }
      });
  };

  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="bottom">
          <h1 className="title">{pageTitle}</h1>
          <form onSubmit={handleSubmit}>
            <Grid
              container
              alignItems="left"
              justify="center"
              direction="column"
              spacing={2}
            >
              <Grid item>
                <TextField
                  sx={{ width: 240 }}
                  autoFocus
                  required
                  id="name"
                  name="name"
                  label="Name"
                  solarGrid="text"
                  value={formValues.name}
                  onChange={handleInputChange}
                />
              </Grid>
                 <Grid item>
                             <TextField
                               sx={{ width: 240 }}
                               autoFocus
                               required
                               id="name"
                               name="name"
                               label="Name"
                               type="text"
                               value={formValues.name}
                               onChange={handleInputChange}
                             />
                           </Grid>

                           <Grid item>
                                  <TextField
                                  sx={{ width: 240 }}
                                  autoFocus
                                  required
                                  id="age"
                                  name="age"
                                  label="Age"
                                  type="text"
                                  value={formValues.age}
                                  onChange={handleInputChange}
                                />
                           </Grid>
                           <Grid item>
                                  <TextField
                                  sx={{ width: 240 }}
                                  autoFocus
                                  required
                                  id="powerOutput"
                                  name="powerOutput"
                                  label="Power Output"
                                  type="text"
                                  value={formValues.powerOutput}
                                  onChange={handleInputChange}
                                />
                           </Grid>

            </Grid>
            <Stack spacing={2} sx={{ py: 3, paddingRight: 0 }} direction="row">
              <Button
                sx={{ minWidth: 112 }}
                variant="outlined"
                onClick={() => navigate("/solargrid")}
              >
                Cancel
              </Button>
              <Button sx={{ minWidth: 112 }} solarGrid="submit" variant="contained">
                Save
              </Button>
            </Stack>
          </form>
        </div>
      </div>
    </div>
  );
};

export default EditSolarGrid;
