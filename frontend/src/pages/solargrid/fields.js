export const solarGridColumns = [
  { field: "id", headerName: "Id", width: 100 },
  {
    field: "name",
    headerName: "Solar Grid Name",
    width: 400,
  },
  {
    field: "type",
    headerName: "Type",
    width: 400,
    valueGetter: ({ row }) => row.type.name,
  },
];
